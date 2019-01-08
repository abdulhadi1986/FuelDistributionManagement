package com.abulzahab.FuelDistributionManagement;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.abulzahab.FuelDistributionManagement.dao.AddressRepo;
<<<<<<< HEAD
import com.abulzahab.FuelDistributionManagement.dao.AdminRepo;
import com.abulzahab.FuelDistributionManagement.dao.RequestRepo;
import com.abulzahab.FuelDistributionManagement.dao.StationRepo;
import com.abulzahab.FuelDistributionManagement.dao.UserRepo;
import com.abulzahab.FuelDistributionManagement.dao.VehicleRepo;
import com.abulzahab.FuelDistributionManagement.model.Address;
import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.DistributionVehicle;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;
import com.abulzahab.FuelDistributionManagement.model.User;
import com.abulzahab.FuelDistributionManagement.services.AdminServices;
import com.abulzahab.FuelDistributionManagement.services.CitizenServices;
import com.abulzahab.FuelDistributionManagement.services.OperationServices;

@Controller
public class ApplicationController {
		
	private Citizen user = new Citizen();
	
	private Operator operator = new Operator();
	
	@Autowired
	private OperationServices operationServices;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CitizenServices citizenService ;
	
	@Autowired
	private AddressRepo addressRepo;
	
	@Autowired 
	private StationRepo stationRepo;
	
	@Autowired
	private AdminServices adminServices;
	
	@Autowired
	private RequestRepo requestRepo;
	
	@Autowired
	private VehicleRepo vehicleRepo;
	
	@Autowired
	private AdminRepo adminRepo;
	
@RequestMapping (value ="/register", method = RequestMethod.GET)
public String showForm(Model model) {
	//get the list of available addresses
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	model.addAttribute("citizen",user);
	return "registration";
}

@RequestMapping (value="/addCitizen" , method = RequestMethod.POST)
public String showUser(@ModelAttribute @Valid Citizen citizen , Errors bindingResult, Model model) {
		//if there is error in the input then return the form again but all the filled data must stay
		if (bindingResult.hasErrors()) {
			//return again list of available addresses 
			List<Address> addresses = addressRepo.findAll();
			model.addAttribute("addresses", addresses);
			//return the filled fields in the form so the user doesn't have to fill in again
			model.addAttribute("citizen",citizen);
			return "registration";
		}
	
		if (citizenService.createCitizen(citizen)) {	
				model.addAttribute("citizen", citizen);
				if (!bindingResult.hasErrors())
					return "success";
		}		
	return "success";
	
	
}


@RequestMapping (value= {"/","/home"}, method = RequestMethod.GET)
public String showCitizenHome(Model model) {
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	
	List<FuelStation> localStation= stationRepo.findByCityAddress(user.getCityAddress());
	List<FuelStation> stations = stationRepo.findByCityAddressCity(user.getCityAddress().getCity());
	model.addAttribute("localStation", localStation);
	model.addAttribute("stations", stations);
	
	List<FuelRequest> fuelRequests = requestRepo.findBySubmittedByAndStatus(user, "pending");
	model.addAttribute("fuelRequests", fuelRequests);
	
	model.addAttribute("UserInfo", currentUser);
	return "citizenhome"; 
}

@RequestMapping (value="/submitrequest" , method = RequestMethod.POST)
public String submitRequest(Model model, FuelRequest fuelRequest) {
	
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	
	fuelRequest.setSubmittedBy(user);
	userRepo.save(user);
	
	if (citizenService.submitFuelRequest(fuelRequest)) {
		model.addAttribute("name", fuelRequest);
		
		return "success";
	}else {
		return "error"; 
	}
}

@RequestMapping (value ="/addoperator", method = RequestMethod.GET)
public String ShowFormOperator (Model model) {
	List<FuelStation> stations = stationRepo.findAll();
	model.addAttribute("stations", stations);
	return "addoperator";
}

@RequestMapping (value="/addoperator" , method = RequestMethod.POST)
public String addOperator(Operator operator) {
	if(adminServices.addOperator(operator))	{
		return "success";
	}
	return "error";
}

// Get add vehicle page
@RequestMapping (value ="/vehicle", method = RequestMethod.GET)
public String ShowFormVehicle(Model model) {
	//get the list of available stations
	List<FuelStation> stations = stationRepo.findAll();
	model.addAttribute("stations", stations);
	return "addvehicle";
}

//Add New Vehicle 
@RequestMapping (value="/vehicle" , method = RequestMethod.POST)
public String AddVehicle (@ModelAttribute @Valid DistributionVehicle vehicle , Errors bindingResult, Model model) {
		//if there is error in the input then return the form again but all the filled data must stay
		if (bindingResult.hasErrors()) {
			//return again list of available stations
			List<FuelStation> stations = stationRepo.findAll();
			model.addAttribute("stations", stations);
			//return the filled fields in the form so the user doesn't have to fill in again
			model.addAttribute("vehicle",vehicle);
			return "addvehicle";
		}
		if(adminServices.addVehicle(vehicle)) {
			
			model.addAttribute("vehicle", vehicle);
			if(!bindingResult.hasErrors())
				return "success";
		}		
	return "success";
	
	
}


//Get add station page
@RequestMapping (value ="/station", method = RequestMethod.GET)
public String ShowFormStation(Model model) {
	//get the list of available addresses
	List<Address> address = addressRepo.findAll();
	//get the list of available operators
	List<Operator> operator = adminRepo.findAll();
	//get the list of available vehicles
	List<DistributionVehicle> vehicle = vehicleRepo.findAll();
	model.addAttribute("addresses", address);
	model.addAttribute("operators", operator);
	model.addAttribute("vehicles", vehicle);
	return "addstation";
}

//Add New Station
@RequestMapping (value="/addstation" , method = RequestMethod.POST)
public String AddStation (@ModelAttribute @Valid FuelStation station , Errors bindingResult, Model model) {
		//if there is error in the input then return the form again but all the filled data must stay
		if (bindingResult.hasErrors()) {
			//return again list of available addresses
			List<Address> address = addressRepo.findAll();
			//get the list of available operators
			List<Operator> operator = adminRepo.findAll();
			//get the list of available vehicles
			List<DistributionVehicle> vehicle = vehicleRepo.findAll();
			model.addAttribute("addresses", address);
			model.addAttribute("operators", operator);
			model.addAttribute("vehicles", vehicle);

			
			//return the filled fields in the form so the user doesn't have to fill in again
	
			model.addAttribute("station",station);
			return "addstation";
		}
		if(adminServices.addStation(station)) {
			
			model.addAttribute("station", station);
			if(!bindingResult.hasErrors())
				return "success";
		}		
	return "success";
	
	
}


// Get Login Page

@RequestMapping (value ="/login", method = RequestMethod.GET)
public String ShowFormLogin (Model model) {
	return "login";
}



// Submit Login Form
@RequestMapping (value="/login" , method = RequestMethod.POST)
public String LoginForm (User user, Model model) {
	String role;
	if(citizenService.IsUser(user))	{
		role = user.getRole();
	}else 
		return "error";
	
	switch (role) {
	case "ADMIN" :
		return "adminPanel";
	case "CITIZEN" :
		return "citizenhome";
	default:
		return "operation";
	}	
 }

@RequestMapping (value= {"/operation"}, method = RequestMethod.GET)
public String showOperationPage(Model model) {
	java.util.Optional<Operator> currentUser = adminRepo.findById("12344");
	if (currentUser.isPresent()) {
		operator = currentUser.get();
	}
	
	
	List<FuelRequest> fuelRequests = requestRepo.findByFuelStationOperatorAndStatus(operator, "pending");
	model.addAttribute("fuelRequests", fuelRequests);
	//model.addAttribute(status, attributeValue);
	
	String status = operationServices.getStationStatus(operator);
	model.addAttribute("status",status);

	return "operation"; 
}

@RequestMapping (value= {"/addfuel"}, method = RequestMethod.POST)
public String AddStationFuel(Model model, double amount) {
	java.util.Optional<Operator> currentUser = adminRepo.findById("12344");
	if (currentUser.isPresent()) {
		operator = currentUser.get();
	}
	
	
	operationServices.addFuel(operator, amount);

	return "operation"; 
}




=======
import com.abulzahab.FuelDistributionManagement.dao.RequestRepo;
import com.abulzahab.FuelDistributionManagement.dao.StationRepo;
import com.abulzahab.FuelDistributionManagement.dao.UserRepo;
import com.abulzahab.FuelDistributionManagement.model.Address;
import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;
import com.abulzahab.FuelDistributionManagement.services.AdminServices;
import com.abulzahab.FuelDistributionManagement.services.CitizenServices;

@Controller
public class ApplicationController {
		
	private Citizen user = new Citizen();
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CitizenServices citizenService ;
	
	@Autowired
	private AddressRepo addressRepo;
	
	@Autowired 
	private StationRepo stationRepo;
	
	@Autowired
	private AdminServices adminServices;
	
	@Autowired
	private RequestRepo requestRepo;
	
@RequestMapping (value ="/register", method = RequestMethod.GET)
public String registerationForm(Model model) {
	//get the list of available addresses
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
	
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	model.addAttribute("citizen",user);
	return "registration";
}

@RequestMapping (value="/addCitizen" , method = RequestMethod.POST)
public String addUser(@ModelAttribute @Valid Citizen citizen , Errors bindingResult, Model model) {
		//if there is error in the input then return the form again but all the filled data must stay
		if (bindingResult.hasErrors()) {
			//return again list of available addresses 
			List<Address> addresses = addressRepo.findAll();
			model.addAttribute("addresses", addresses);
			//return the filled fields in the form so the user doesn't have to fill in again
			model.addAttribute("citizen",citizen);
			return "registration";
		}
	
		if (citizenService.createCitizen(citizen)) {	
				model.addAttribute("citizen", citizen);
				if (!bindingResult.hasErrors())
					return "success";
		}		
	return "success";
	
	
}

@RequestMapping (value ="/profile", method = RequestMethod.GET)
public String editCitizenProfile(Model model) {
	//get the list of available addresses
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
	
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	model.addAttribute("citizen",user);
	return "profile";
}


@RequestMapping (value="/savecitizenprofile" , method = RequestMethod.POST)
public String editUser(@ModelAttribute @Valid Citizen citizen , Errors bindingResult, Model model) {
		//if there is error in the input then return the form again but all the filled data must stay
		if (bindingResult.hasErrors()) {
			//return again list of available addresses 
			List<Address> addresses = addressRepo.findAll();
			model.addAttribute("addresses", addresses);
			//return the filled fields in the form so the user doesn't have to fill in again
			model.addAttribute("citizen",citizen);
			return "registration";
		}
	
		if (citizenService.createCitizen(citizen)) {	
				model.addAttribute("citizen", citizen);
				if (!bindingResult.hasErrors())
					return "citizenhome";
		}		
	return "success";
	
	
}

@RequestMapping (value= {"/","/home"}, method = RequestMethod.GET)
public String showCitizenHome(Model model) {
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	
	List<FuelStation> localStation= stationRepo.findByCityAddress(user.getCityAddress());
	List<FuelStation> stations = stationRepo.findByCityAddressCity(user.getCityAddress().getCity());
	model.addAttribute("localStation", localStation);
	model.addAttribute("stations", stations);
	
	List<FuelRequest> fuelRequests = requestRepo.findBySubmittedByAndStatus(user, "pending");
	model.addAttribute("fuelRequests", fuelRequests);
	return "citizenhome"; 
}

@RequestMapping (value="/submitrequest" , method = RequestMethod.POST)
public String submitRequest(Model model, FuelRequest fuelRequest) {
	
	java.util.Optional<Citizen> currentUser = userRepo.findById("12345");
	if (currentUser.isPresent()) {
		user = currentUser.get();
	}
	
	fuelRequest.setSubmittedBy(user);
	userRepo.save(user);
	
	if (citizenService.submitFuelRequest(fuelRequest)) {
		model.addAttribute("name", fuelRequest);
		
		return "success";
	}else {
		return "error"; 
	}
}

@RequestMapping (value ="/addoperator", method = RequestMethod.GET)
public String ShowFormOperator (Model model) {
	List<FuelStation> stations = stationRepo.findAll();
	model.addAttribute("stations", stations);
	return "addoperator";
}

@RequestMapping (value="/addoperator" , method = RequestMethod.POST)
public String addOperator(Operator operator) {
	if(adminServices.addOperator(operator))	{
		return "success";
	}
	return "error";
}
>>>>>>> branch 'Wael' of https://github.com/abdulhadi1986/FuelDistributionManagement.git

}//main
