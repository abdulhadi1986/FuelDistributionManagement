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
import com.abulzahab.FuelDistributionManagement.dao.CitizenRepo;
import com.abulzahab.FuelDistributionManagement.dao.VehicleRepo;
import com.abulzahab.FuelDistributionManagement.model.Address;
import com.abulzahab.FuelDistributionManagement.model.Admin;
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
	
	private User loggedUser ;
	private String role="";
	//java.util.Optional<User> currentUser = userRepo.findById("12345");	
	
	@Autowired
	private CitizenRepo citizenRepo;
	
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
	
	@Autowired
	private UserRepo userRepo;

@RequestMapping (value = "/logout", method = RequestMethod.GET)
public String logout() {
	loggedUser = null; 
	role = "";
	return "login";
}
	
@RequestMapping (value= "/login" , method = RequestMethod.GET)
public String getLogin(Model model) {
	if (loggedUser != null) {
		return "redirect:/home";
	}
	model.addAttribute("errorMessage", "");
	return "login";
}

@RequestMapping (value="/checklogin" , method= RequestMethod.POST)
public String loginCheck(String userName, String password, Model model) {
	if (userRepo.existsByUserNameAndPassword(userName, password)) {
		role = userRepo.findByUserNameAndPassword(userName, password).getRegisteredRole();
		loggedUser = userRepo.findByUserNameAndPassword(userName, password);
		return "redirect:/home";
	}
	
	model.addAttribute("errorMessage", "username and/or password are incorrect");
	return "login";
}

@RequestMapping (value= {"/","/home"} , method= RequestMethod.GET)
public String getHomePage(Model model) {
	if (role.equals("citizen")) {
		return "redirect:/citizenhome";
	}else if (role.equals("operator")) {
		return "redirect:/operatorhome";
	}else if (role.equals("admin")) {
		return "redirect:/adminhome";
	}
	
	return "login";
}

	
@RequestMapping (value ="/register", method = RequestMethod.GET)
public String registerationForm(Model model) {
	//get the list of available addresses
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
	Citizen citizen;
	if(loggedUser != null) {
		citizen = (Citizen) loggedUser;
	}else {
		citizen = new Citizen();
	}
	model.addAttribute("citizen",citizen);
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
	 	if (citizenRepo.existsById(citizen.getNationalNo())) {
	 		model.addAttribute("citizen",citizen);
	 		List<Address> addresses = addressRepo.findAll();
			model.addAttribute("addresses", addresses);
	 		return "userexists";
	 	}
	 		
	 	
		if (citizenService.createCitizen(citizen)) {	
				model.addAttribute("citizen", citizen);
				if (!bindingResult.hasErrors())
					return "login";
		}		
	return "login";
  	
	
}

@RequestMapping (value ="/profile", method = RequestMethod.GET)
public String getCitizenProfile(Model model) {
	//get the list of available addresses
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
	Citizen citizen;
	
	if (!role.equals("citizen")) {
		return "login";
	}
	citizen = (Citizen) loggedUser;

	model.addAttribute("citizen",citizen);
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

@RequestMapping (value= "/citizenhome" , method = RequestMethod.GET)
public String showCitizenHome(Model model) {
	Citizen citizen;
	
	if (!role.equals("citizen")) {
		return "login";
	}
	citizen = (Citizen) loggedUser;
	
	String noRequests="";
	
	List<FuelStation> localStation= stationRepo.findByCityAddress(citizen.getCityAddress());
	List<FuelStation> stations = stationRepo.findByCityAddressCity(citizen.getCityAddress().getCity());
	model.addAttribute("localStation", localStation);
	model.addAttribute("stations", stations);
	
	List<FuelRequest> fuelRequests = requestRepo.findBySubmittedByAndStatus(citizen, "pending");
	model.addAttribute("fuelRequests", fuelRequests);
	if (fuelRequests.size()<1)
		noRequests = "You dont have any pending request.";
	model.addAttribute("noRequests", noRequests);
	model.addAttribute("pendingRequests", fuelRequests.size());
	return "citizenhome"; 
}

@RequestMapping (value="/submitrequest" , method = RequestMethod.POST)
public String submitRequest(Model model, FuelRequest fuelRequest) {
	Citizen citizen;
	
	if (!role.equals("citizen")) {
		return "login";
	}
	citizen = (Citizen) loggedUser;
	
	fuelRequest.setSubmittedBy(citizen);
	citizenRepo.save(citizen);
	
	if (citizenService.submitFuelRequest(fuelRequest)) {
		model.addAttribute("name", fuelRequest);
		
		return "redirect:/home";
	}else {
		return "error"; 
	}
}

@RequestMapping (value="/histroy", method = RequestMethod.GET)
public String getUserReports(Model model) {
Citizen citizen;
	
	if (!role.equals("citizen")) {
		return "login";
	}
	citizen = (Citizen) loggedUser;
	
	List<FuelRequest> allFuelRequests= requestRepo.findBySubmittedBy(citizen);
	model.addAttribute("allFuelRequests", allFuelRequests);
	return "userreports";
}

@RequestMapping (value = "/adminhome" , method = RequestMethod.GET)
public String getAdminHome(Model model) {
	Admin admin;
	
	if (!role.equals("admin")) {
		return "login";
	}
	admin = (Admin) loggedUser;
	return "adminhome";
}

@RequestMapping (value ="/addoperator", method = RequestMethod.GET)
public String ShowFormOperator (Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	Admin admin;
	
	if (!role.equals("admin")) {
		return "login";
	}
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
	
	return "redirect:/addoperator";
}

@RequestMapping (value="/deloperator", method = RequestMethod.GET , params= {"nationalNo"})
public String delOperator(Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	operatorRepo.deleteById(Integer.toString(nationalNo));
	return "redirect:/addoperator"; 
}
 
@RequestMapping (value="/addstation" , method = RequestMethod.GET)
public String getManageStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	Admin admin;
	
	if (!role.equals("admin")) {
		return "login";
	}	
	List<FuelStation> allStations = stationRepo.findAll();
	model.addAttribute("allStations", allStations);
	
	List<Address> addresses = addressRepo.findAll();
	model.addAttribute("addresses", addresses);
	
	FuelStation fuelStation;
	List<Operator> operators=new ArrayList<Operator>();
	List<DistributionVehicle> vehicles= new ArrayList<DistributionVehicle>(); 
	if (stationId != 0) {
		fuelStation = stationRepo.findById(stationId).orElse(new FuelStation());
		operators.add(operatorRepo.findByFuelStation(fuelStation));
		vehicles=vehicleRepo.findByFuelStation(fuelStation);
		
	} else { 
		fuelStation = new FuelStation();
	}
	if (operators.size()<1)
		operators=operatorRepo.findByFuelStationIsNull();
	if (vehicles.size()<1) 
		vehicles = vehicleRepo.findByFuelStationIsNull();
	
	model.addAttribute("fuelStation", fuelStation);
	model.addAttribute("operators", operators);
	model.addAttribute("vehicles", vehicles);
	
	return "addstation";
}

@RequestMapping (value="/addstation", method= RequestMethod.POST)
public String saveStation(FuelStation fuelStation) {
	stationRepo.save(fuelStation);
	List<DistributionVehicle> vehicles = fuelStation.getDistributionVehicles(); 
	for (DistributionVehicle vehicle : vehicles) {
		vehicle.setFuelStation(fuelStation);
		vehicleRepo.save(vehicle);
	}
	return "redirect:/addstation";
}

@RequestMapping (value="/delstation" , method = RequestMethod.GET, params= {"stationId"})
public String deleteStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	if (stationId != 0) {
		
		List<DistributionVehicle> vehicles = vehicleRepo.findByFuelStation(stationRepo.findById(stationId).get());
		for (DistributionVehicle vehicle : vehicles) {
			vehicle.setFuelStation(null);
			vehicleRepo.save(vehicle);
		}
		Operator operator = operatorRepo.findByFuelStation(stationRepo.findById(stationId).get());
		operator.setFuelStation(null);
		operatorRepo.save(operator);
		
		stationRepo.deleteById(stationId);
	}  
	return "redirect:/addstation";
}

@RequestMapping (value="/adminreports" , method = RequestMethod.GET)
public String getAdminReports(Model model) {
	Admin admin;
	
	if (!role.equals("admin")) {
		return "login";
	}
	
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
	Admin admin;
	
	if (!role.equals("admin")) {
		return "login";
	}
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
	Operator operator;
	
	if (!role.equals("operator")) {
		return "login";
	}	
	
	operator = (Operator) loggedUser;
	List<FuelRequest> allFuelReuests = requestRepo.findByFuelStationAndStatus(operator.getFuelStation(), "pending");
	model.addAttribute("allFuelReuests", allFuelReuests);
	FuelRequest fuelRequest = requestRepo.findById(requestId).orElse(new FuelRequest());
	fuelRequest.setApprovedAmount(fuelRequest.getAmount());
	model.addAttribute("fuelRequest", fuelRequest);
	List<DistributionVehicle> vehicles= vehicleRepo.findByFuelStation(fuelRequest.getFuelStation());
	model.addAttribute("vehicles", vehicles);
	
	Citizen submittedBy = fuelRequest.getSubmittedBy();
	model.addAttribute("citizen", submittedBy);

	return "operation";
}

@RequestMapping (value="/approverequest" , method = RequestMethod.POST)
public String approveRequest(@Valid FuelRequest fuelRequest, Errors bindingResult) {
	
	Operator operator = (Operator) loggedUser;
	if (fuelRequest.getRequestId()==0)
		return "redirect:/operatorhome";
	
	fuelRequest.setApprovedBy(operator);
	fuelRequest.setStatus("processed");
	requestRepo.save(fuelRequest);
	return "redirect:/operatorhome"; 
}


@RequestMapping (value ="/addvehicle", method = RequestMethod.GET)
public String getAddVehiclePage(Model model, @RequestParam(value="vehicleId", required=false, defaultValue= "0")int vehicleId) {
	
	//get all available vehicles
	List<DistributionVehicle> allvehicles = vehicleRepo.findAll();
	model.addAttribute("allvehicles", allvehicles);
	
	//get the list of available stations
	List<FuelStation> stations = stationRepo.findAll();
	model.addAttribute("stations", stations);
	
	DistributionVehicle vehicle;
	if(vehicleId!=0) {
		vehicle = vehicleRepo.findById(vehicleId).orElse(new 	DistributionVehicle());
	}else {
	vehicle = new DistributionVehicle();
	model.addAttribute("distributionVehicle", vehicle);
	}
	return "addvehicle";
}


// Post add Vehicle form
@RequestMapping (value="/addvehicle", method= RequestMethod.POST)
public String savevehicle1(DistributionVehicle vehicle) {
	vehicleRepo.save(vehicle);
	return "redirect:/addvehicle";
}

@RequestMapping (value="/delvehicle" , method = RequestMethod.GET, params= {"vehicleId"})
public String deleteVehicle(Model model, @RequestParam(value="vehicleId", required=false, defaultValue= "0")int vehicleId) {
	if (vehicleId != 0) {
		vehicleRepo.deleteById(vehicleId);
	}  
	return "redirect:/addvehicle";
}



}//main
