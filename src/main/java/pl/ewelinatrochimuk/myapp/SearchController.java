package pl.ewelinatrochimuk.myapp;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SearchController {

	

	@RequestMapping(value = "/bla", method = RequestMethod.GET)
	public String showLoginPage(){
		
		return "search";
		
	}

	
	@RequestMapping(value = "/result", method = RequestMethod.POST)
	@ResponseBody
	public String doPOST(){
		
		return "NO TO KLIKNIETE";
		
	}
		
	
	
	
}
