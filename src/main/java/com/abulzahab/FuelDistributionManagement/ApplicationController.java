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
import org.springframework.web.bind.annotation.RequestParam;

import com.abulzahab.FuelDistributionManagement.dao.AddressRepo;
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
	 
	    //check if the user already exists
	 	if (userRepo.existsById(citizen.getNationalNo())) {
	 		model.addAttribute("citizen",citizen);
	 		return "userexists";
	 	}
	 		
	 	
		if (citizenService.createCitizen(citizen)) {	
				model.addAttribute("citizen", citizen);
				if (!bindingResult.hasErrors())
					return "success";
		}		
	return "success";
  	
	
}

@RequestMapping (value ="/profile", method = RequestMethod.GET)
public String getCitizenProfile(Model model) {
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
public String editCitizenProfile(@ModelAttribute @Valid Citizen citizen , Errors bindingResult, Model model) {
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

@RequestMapping (value="/addstation" , method = RequestMethod.GET, params= {"stationId"})
public String getManageStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	List<FuelStation> allStations = stationRepo.findAll();
	model.addAttribute("allStations", allStations);
	FuelStation fuelStation;
	if (stationId != 0) {
		fuelStation = stationRepo.findById(stationId).orElse(new FuelStation());
	} else 
		fuelStation = new FuelStation(); 
	model.addAttribute("fuelStation", fuelStation);
	
	return "addstation";
}

@RequestMapping (value="/addstation", method= RequestMethod.POST)
public String saveStation(FuelStation fuelStation) {
	stationRepo.save(fuelStation);
	
	return "redirect:/addstation";
}

@RequestMapping (value="/delstation" , method = RequestMethod.GET, params= {"stationId"})
public String deleteStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	if (stationId != 0) {
		stationRepo.deleteById(stationId);
	}  
	return "redirect:/addstation";
}

}//main
