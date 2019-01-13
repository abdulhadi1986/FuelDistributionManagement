package com.abulzahab.FuelDistributionManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	try {
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
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/addCitizen" , method = RequestMethod.POST)
public String addUser(@ModelAttribute @Valid Citizen citizen , Errors bindingResult, Model model) {
	try {
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
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
  	
	
}

@RequestMapping (value ="/profile", method = RequestMethod.GET)
public String getCitizenProfile(Model model) {
	try {
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
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}


@RequestMapping (value="/savecitizenprofile" , method = RequestMethod.POST)
public String editCitizenProfile(@ModelAttribute @Valid Citizen citizen , Errors bindingResult, Model model) {
	try {	
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
					return "redirect:/citizenhome";
		}		
	return "redirect:/citizenhome";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
	
}

@RequestMapping (value= "/citizenhome" , method = RequestMethod.GET)
public String showCitizenHome(Model model) {
	try {
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
	
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/submitrequest" , method = RequestMethod.POST)
public String submitRequest(Model model, FuelRequest fuelRequest) {
	try {
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
	
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/histroy", method = RequestMethod.GET)
public String getUserReports(Model model) {
	try {
	Citizen citizen;
	if (!role.equals("citizen")) {
		return "login";
	}
	citizen = (Citizen) loggedUser;
	
	List<FuelRequest> allFuelRequests= requestRepo.findBySubmittedBy(citizen);
	model.addAttribute("allFuelRequests", allFuelRequests);
	return "userreports";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value = "/adminhome" , method = RequestMethod.GET)
public String getAdminHome(Model model) {
	
	try {
	Admin admin;
	
	if (!role.equals("admin")) {
		return "login";
	}
	admin = (Admin) loggedUser;
	return "adminhome";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value ="/addoperator", method = RequestMethod.GET)
public String ShowFormOperator (Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	try {
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
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
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
	Optional<Operator> operator = operatorRepo.findById(Integer.toString(nationalNo));
	FuelStation fuelStation=null;
	if (operator != null)
		fuelStation = stationRepo.findByOperator(operator);
	fuelStation.setOperator(null);
	stationRepo.save(fuelStation);
	
	return "redirect:/addoperator"; 
}
 
@RequestMapping (value="/addstation" , method = RequestMethod.GET)
public String getManageStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	try {
	
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
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/addstation", method= RequestMethod.POST)
public String saveStation(FuelStation fuelStation, Model model) {
	try {
	stationRepo.save(fuelStation);
	List<DistributionVehicle> vehicles = fuelStation.getDistributionVehicles(); 
	for (DistributionVehicle vehicle : vehicles) {
		vehicle.setFuelStation(fuelStation);
		vehicleRepo.save(vehicle);
	}
	return "redirect:/addstation";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/delstation" , method = RequestMethod.GET, params= {"stationId"})
public String deleteStations(Model model, @RequestParam(value="stationId", required=false, defaultValue= "0")int stationId) {
	try {
	
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
	
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value= {"/filterreport","/adminreports"}, method= RequestMethod.GET)
public String getAdminFilteredReports(Model model 
									,@RequestParam(value="fuelStationId", required = false, defaultValue="0")int fuelStationId
									,@RequestParam(value="dateFrom", required = false, defaultValue="")String dateF
									,@RequestParam(value="dateTo", required = false, defaultValue="")String dateT) {
	try {
	Admin admin;
	int orderCount = 0;
	int totalLiters =0;
	
	LocalDate dateFrom,dateTo;
	//set the dates to filter 
	if(dateF.equals("")) {
		dateFrom = LocalDate.parse("1950-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}else {
		dateFrom = LocalDate.parse(dateF, DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
	}
	if(dateT.equals("")) {
		dateTo = LocalDate.now(); 
	}else {
		dateTo = LocalDate.parse(dateT, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	if (!role.equals("admin")) {
		return "login";
	}
	
	Map<FuelStation,List<FuelRequest>> allStationsMap = new HashMap<FuelStation, List<FuelRequest>>();
	List<FuelStation> allStations = stationRepo.findAll();
	List<FuelStation> filteredStations = new ArrayList<FuelStation>();
	
		
	if (fuelStationId !=0) {
		FuelStation fuelStation = stationRepo.findById(fuelStationId).orElse(new FuelStation());
		allStationsMap.put(fuelStation, requestRepo.findByFuelStation(fuelStation));
		//allStations.add(fuelStation);
		}
	else {
		filteredStations = allStations;
		for (FuelStation fuelStation : filteredStations) {
			List<FuelRequest> allFuelRequests = requestRepo.findByFuelStation(fuelStation);
			List<FuelRequest> filteredRequests = new ArrayList<FuelRequest>();
            if (allFuelRequests.size()>0) {
            	for (FuelRequest request : allFuelRequests) {
            		if (request.getSubmitionDate().isAfter(dateFrom.minusDays(1)) && request.getSubmitionDate().isBefore(dateTo.plusDays(1))) {
            			filteredRequests.add(request);
            			
            		}
            	}
            	allStationsMap.put(fuelStation,filteredRequests);
			    //allStationsMap.put(fuelStation, requestRepo.findByFuelStationAndSubmitionDateLessThanAndSubmitionDateGreaterThan(fuelStation,dateTo, dateFrom));
		    }
			
		}
	}
    for (List<FuelRequest> fuelRequests : allStationsMap.values()) {
		orderCount+= fuelRequests.size();
		for (FuelRequest fuelRequest:fuelRequests) {
			totalLiters+=fuelRequest.getApprovedAmount();
		}
	}
	
	model.addAttribute("orderCount", orderCount);
	model.addAttribute("totalLiters", totalLiters);
	
	model.addAttribute("allStations", allStations);
	model.addAttribute("allStationsMap", allStationsMap);
	return "adminreports"; 
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}
@RequestMapping (value="/operatorhome" , method = RequestMethod.GET)
public String getOperationHomePage(Model model, @RequestParam(value="requestId" , required = false, defaultValue="0")int requestId) {
	try {
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
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/approverequest" , method = RequestMethod.POST)
public String approveRequest(@Valid FuelRequest fuelRequest, Errors bindingResult, Model model) {
	try {
	Operator operator = (Operator) loggedUser;
	if (fuelRequest.getRequestId()==0)
		return "redirect:/operatorhome";
	
	fuelRequest.setApprovedBy(operator);
	fuelRequest.setStatus("processed");
	if (fuelRequest.getDeliveryDate()== null)
		fuelRequest.setDeliveryDate(fuelRequest.getPreferedDeliveryDate());
	
	requestRepo.save(fuelRequest);
	return "redirect:/operatorhome"; 
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/operatorreports" , method = RequestMethod.GET)
public String getOperatorReports(Model model) {
	try {
	Operator operator;
	if (!role.equals("operator")) {
		return "login";
	}	
	operator = (Operator) loggedUser;
	List<FuelRequest> allFuelRequests= requestRepo.findByFuelStation(operator.getFuelStation());
	model.addAttribute("allFuelRequests", allFuelRequests);

	return "operatorreports";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value ="/addvehicle", method = RequestMethod.GET)
public String getAddVehiclePage(Model model, @RequestParam(value="vehicleId", required=false, defaultValue= "0")int vehicleId) {
	try {
	//get all available vehicles
	List<DistributionVehicle> allvehicles = vehicleRepo.findByFuelStationNotNull();
	model.addAttribute("allvehicles", allvehicles);
	List<DistributionVehicle> unAssvehicles = vehicleRepo.findByFuelStationIsNull();
	model.addAttribute("unAssvehicles", unAssvehicles);
	
	
	//get the list of available stations
	List<FuelStation> stations = stationRepo.findAll();
	model.addAttribute("stations", stations);
	
	DistributionVehicle vehicle;
	if(vehicleId!=0) {
		vehicle = vehicleRepo.findById(vehicleId).orElse(new 	DistributionVehicle());
	}else {
	vehicle = new DistributionVehicle();
	
	}
	model.addAttribute("distributionVehicle", vehicle);
	return "addvehicle";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}


// Post add Vehicle form
@RequestMapping (value="/addvehicle", method= RequestMethod.POST)
public String savevehicle1(DistributionVehicle vehicle, Model model) {
	try {
	vehicleRepo.save(vehicle);
	return "redirect:/addvehicle";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/delvehicle" , method = RequestMethod.GET, params= {"vehicleId"})
public String deleteVehicle(Model model, @RequestParam(value="vehicleId", required=false, defaultValue= "0")int vehicleId) {
	try {
	if (vehicleId != 0) {
		vehicleRepo.deleteById(vehicleId);
	}  
	return "redirect:/addvehicle";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}


///////// Added By WAEL



@RequestMapping (value ="/addadmin", method = RequestMethod.GET)
public String ShowFormAdmin (Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	
	if (!role.equals("admin")) {
		return "login";
	}
	List<Admin> alladmins = adminRepo.findAll();
	model.addAttribute("alladmins", alladmins);
	Admin admin = adminRepo.findById(Integer.toString(nationalNo)).orElse(new Admin());
	model.addAttribute("admin", admin);

	return "addadmin";
}

@RequestMapping (value="/addadmin" , method = RequestMethod.POST)
public String addAdmin(Admin admin, Model model) {
	try {
	if(adminServices.addAdmin(admin))	{
		return "redirect:/addadmin";
	}
	
	return "redirect:/addadmin";
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}

@RequestMapping (value="/deladmin", method = RequestMethod.GET , params= {"nationalNo"})
public String delAdmin(Model model, @RequestParam(value="nationalNo", required=false, defaultValue= "0")int nationalNo) {
	try {
	adminRepo.deleteById(Integer.toString(nationalNo));
	return "redirect:/addadmin"; 
	}catch(Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
}



}//main
