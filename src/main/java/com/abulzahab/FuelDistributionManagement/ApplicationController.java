package com.abulzahab.FuelDistributionManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.abulzahab.FuelDistributionManagement.dao.AdminRepo;
import com.abulzahab.FuelDistributionManagement.dao.OperatorRepo;
import com.abulzahab.FuelDistributionManagement.dao.RequestRepo;
import com.abulzahab.FuelDistributionManagement.dao.StationRepo;
import com.abulzahab.FuelDistributionManagement.dao.UserRepo;
import com.abulzahab.FuelDistributionManagement.dao.VehicleRepo;
import com.abulzahab.FuelDistributionManagement.model.Address;
import com.abulzahab.FuelDistributionManagement.model.Citizen;
import com.abulzahab.FuelDistributionManagement.model.FuelRequest;
import com.abulzahab.FuelDistributionManagement.model.FuelStation;
import com.abulzahab.FuelDistributionManagement.model.Operator;
import com.abulzahab.FuelDistributionManagement.services.AdminServices;
import com.abulzahab.FuelDistributionManagement.services.CitizenServices;
import com.abulzahab.FuelDistributionManagement.services.OperationServices;


@Controller
public class ApplicationController {
		
	private Citizen user = new Citizen();
	private Operator operator = new Operator();
	
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
	private OperatorRepo operatorRepo;
	
	@Autowired
	private OperationServices operationServices;
	
	@Autowired
	private AdminRepo adminRepo;
	
	@Autowired
	private VehicleRepo vehicleRepo;
	
	
	
	
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

@RequestMapping (value = "/adminhome" , method = RequestMethod.GET)
public String getAdminHome(Model model) {
	
	return "adminhome";
}

@RequestMapping (value ="/addoperator", method = RequestMethod.GET)
public String ShowFormOperator (Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	List<FuelStation> stations = stationRepo.findAll();
	model.addAttribute("stations", stations);
	List<Operator> allOperators = operatorRepo.findAll();
	model.addAttribute("allOperators", allOperators);
	
	Operator operator = operatorRepo.findById(Integer.toString(nationalNo)).orElse(new Operator());
	model.addAttribute("operator", operator);
	return "addoperator";
}

@RequestMapping (value="/addoperator" , method = RequestMethod.POST)
public String addOperator(Operator operator) {
	if(adminServices.addOperator(operator))	{
		return "redirect:/addoperator";
	}
	return "error";
}

@RequestMapping (value="/deloperator", method = RequestMethod.GET , params= {"nationalNo"})
public String delOperator(Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	operatorRepo.deleteById(Integer.toString(nationalNo));
	return "redirect:/addoperator"; 
}
 
@RequestMapping (value="/addstation" , method = RequestMethod.GET)
public String getManageStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	List<FuelStation> allStations = stationRepo.findAll();
	model.addAttribute("allStations", allStations);
	//return again list of available addresses 
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
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

@RequestMapping (value="/adminreports" , method = RequestMethod.GET)
public String getAdminReports(Model model) {
	Map<FuelStation,List<FuelRequest>> allStationsMap = new HashMap<FuelStation, List<FuelRequest>>();
	List<FuelStation> allStations = stationRepo.findAll();
	for (FuelStation fuelStation : allStations) {
		allStationsMap.put(fuelStation, requestRepo.findByFuelStation(fuelStation));
	}
	model.addAttribute("allStations", allStations);
	model.addAttribute("allStationsMap", allStationsMap);
	return "adminreports"; 
}

@RequestMapping (value="/filterreport", method= RequestMethod.GET)
public String getAdminFilteredReports(Model model 
									,@RequestParam(value="fuelStationId", required = false, defaultValue="0")int fuelStationId
									,@RequestParam(value="dateFrom", required = false, defaultValue="1950-01-01")String dateF
									,@RequestParam(value="dateTo", required = false, defaultValue="")String dateT) {
	Map<FuelStation,List<FuelRequest>> allStationsMap = new HashMap<FuelStation, List<FuelRequest>>();
	List<FuelStation> allStations = new ArrayList<FuelStation>();
	
	LocalDate dateFrom , dateTo;
	if(dateT.equals("")) {
		dateTo = LocalDate.now(); 
	}else {
		dateTo = LocalDate.parse(dateT, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	dateFrom = LocalDate.parse(dateF, DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
	
		
	if (fuelStationId !=0) {
		FuelStation fuelStation = stationRepo.findById(fuelStationId).orElse(new FuelStation());
		allStationsMap.put(fuelStation, requestRepo.findByFuelStation(fuelStation));
		allStations.add(fuelStation);
		}
	else {
		allStations = stationRepo.findAll();
		for (FuelStation fuelStation : allStations) {
			allStationsMap.put(fuelStation, requestRepo.findBySubmitionDateLessThanAndSubmitionDateGreaterThan(dateTo, dateFrom));
		requestRepo.findBySubmitionDateLessThanAndSubmitionDateGreaterThan(dateTo, dateFrom);
		}
	}
	
	model.addAttribute("allStations", allStations);
	model.addAttribute("allStationsMap", allStationsMap);
	return "adminreports"; 
}

@RequestMapping (value="/operatorhome" , method = RequestMethod.GET)
public String getOperationHomePage(Model model, @RequestParam(value="requestId" , required = false, defaultValue="0")int requestId) {
	Operator operator = operatorRepo.findById("12344").orElse(new Operator());
	List<FuelRequest> allFuelReuests = requestRepo.findByFuelStation(operator.getFuelStation());
	model.addAttribute("allFuelReuests", allFuelReuests);
	FuelRequest fuelRequest = requestRepo.findById(requestId).orElse(new FuelRequest());
	model.addAttribute("fuelRequest", fuelRequest);

	return "operation";
}

@RequestMapping (value="/approverequest" , method = RequestMethod.POST)
public String approveRequest(FuelRequest fuelRequest) {
	requestRepo.save(fuelRequest);
	return "redirect:/operatorhome"; 
}
/*
//operation 
@RequestMapping (value= {"/operation"}, method = RequestMethod.GET)
public String getOperationHomePage(Model model) {
	java.util.Optional<Operator> currentUser = adminRepo.findById("12344");
	if (currentUser.isPresent()) {
		operator = currentUser.get();
	}
	
	List<FuelRequest> fuelRequests = requestRepo.findByFuelStationOperatorAndStatus(operator, "pending");
	model.addAttribute("fuelRequests", fuelRequests);
	//model.addAttribute(status, attributeValue);
	
	String status = operationServices.getStationStatus(operator);
	model.addAttribute("status",status);
	
	List<DistributionVehicle> vehicle = vehicleRepo.findAll();
	model.addAttribute("vehicles", vehicle);
	model.addAttribute("fullname", operator);

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

@RequestMapping (value= {"/approverequest"}, method = RequestMethod.POST)
public String ChangeRequest(Model model, FuelRequest fuelrequest) {
	java.util.Optional<Operator> currentUser = adminRepo.findById("12344");
	if (currentUser.isPresent()) {
		operator = currentUser.get();
	}
	fuelrequest.setApprovedBy(operator);
	requestRepo.save(fuelrequest);
	return "operation"; 
}
*/
}//main
