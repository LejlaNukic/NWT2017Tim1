package com.example.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.DELETE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.models.Discussion;
import com.example.models.Interest;
import com.example.models.RegisteredUser;
import com.example.repositories.DiscussionRepository;
import com.example.repositories.InterestRepository;
import com.example.repositories.UserRepository;

@RestController
public class DiscussionController {

	@Autowired
	private DiscussionRepository dr;
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private InterestRepository ir;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RegisteredUserController ruc;
	
	@RequestMapping("/delete")
	public int deleteDiscussion(@RequestParam(value="id") Long id, @RequestParam(value="username") String username){
		
		Boolean logovan=false;
		logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+username,Boolean.class);
		
		if (logovan==false){
			return 0;
		}
		
		Discussion d=dr.findOne(id);
		Boolean admin=false;
		
		List<String> roles=this.restTemplate.getForObject("http://users-client/user/roles?username="+username,List.class);
		admin=roles.contains("admin");
		
	
		if(d.getRegUser().getUsername()==username || admin==true){
			dr.delete(d);
			return 1;
		}
		
		return 0;
	}
	
	@RequestMapping("/create")
	public Discussion createDiscussion(@RequestBody DiscussionBody discussion) throws ServletException{
		
		Boolean logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+discussion.username,Boolean.class);
		
		if(logovan==false){
			throw new ServletException("Niste logovani");
		}
    
   	
    	Discussion d=new Discussion();
    	d.setOpen(true);
    	d.setText(discussion.tekst);
    	d.setTitle(discussion.naziv);
    	
    	RegisteredUser user=ur.findByUsername(discussion.username);
    	
    	d.setRegUser(user);
    	dr.save(d);
    	return d;		
		
	}
	
	@RequestMapping("/changestatus")
	public Boolean closeDiscussion(@RequestParam(value="id") Long id,@RequestParam(value="username") String username) throws ServletException{
		
		Boolean logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+username,Boolean.class);
		
		if(logovan==false){
			throw new ServletException("Niste logovani");
		}
		
		RegisteredUser user=ur.findByUsername(username);
		Discussion d=dr.findOne(id);
		
		Boolean admin=false;
		
		List<String> roles=this.restTemplate.getForObject("http://users-client/user/roles?username="+username,List.class);
		admin=roles.contains("admin");
		
		
		if(user.getUsername()==d.getRegUser().getUsername() || admin==true){
			d.setOpen(!d.getOpen());
			dr.save(d);
			return true;
		}
		
		return false;
		
	}
	
	@RequestMapping("/userdiscussions")
	public List<Discussion> userDiscussions(@RequestParam(value="username") String username,@RequestParam(value="status",required=false) Boolean status) throws ServletException{
		
		List<Discussion> diskusije;
		Boolean logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+username,Boolean.class);
		
		if(logovan==false){
			throw new ServletException("Niste logovani");
		}
		
		if(status!=null){
			diskusije=dr.getDiscussionsByUsernameAndStatus(username, status);
		}
		else{
			diskusije=dr.getDiscussionsByUsername(username);
		}
		return diskusije;
	}
	
	@RequestMapping("/getdiscussions")
	public List<Discussion> getDiscussions(@RequestParam(value="status",required=false) Boolean status){
		List<Discussion> diskusije;
		
		if(status==null){
			diskusije=(List<Discussion>) dr.findAll();
		}
		else{
			diskusije=dr.getDiscussionsByStatus(status);
		}
		
		return diskusije;
	}
	
	@RequestMapping("/interestingdiscussions")
	public List<Discussion> getDiscussionsByInterestedInStatus(@RequestParam(value="username") String username) throws ServletException{
		
		List<Discussion> diskusije;
		Boolean logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+username,Boolean.class);
		
		if(logovan==false){
			throw new ServletException("Niste logovani");
		}
		
		diskusije=dr.getInterestingDiscussions(username);
		return diskusije;
		
	}
	
	@RequestMapping("/addinterest")
	public Boolean addInterest(@RequestParam(value="username") String username,@RequestParam(value="id") Long id)throws ServletException{
		
		List<Discussion> diskusije;
		
		Boolean logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+username,Boolean.class);
		
		if(logovan==false){
			throw new ServletException("Niste logovani");
		}
		
		RegisteredUser user=ur.findByUsername(username);
		Discussion d=dr.findOne(id);
		
		Interest i=new Interest();
		i.setDiscuss(d);
		i.setRegUser(user);
		ir.save(i);
		return true;
		
		
	}
	
	@RequestMapping("/deleteinterest")
	public Boolean deleteInterest(@RequestParam(value="username") String username,@RequestParam(value="id") Long id)throws ServletException{
		
		List<Discussion> diskusije;
		
		Boolean logovan=this.restTemplate.getForObject("http://users-client/user/logged?username="+username,Boolean.class);
		
		if(logovan==false){
			throw new ServletException("Niste logovani");
		}
		
		RegisteredUser user=ur.findByUsername(username);
		Discussion d=dr.findOne(id);
		
		ir.deleteinterest(username,id);
		return true;
		
		
	}
	
	/*@RequestMapping("/populardiscussions")
	public List<Object> getPopularDiscussions(@RequestParam(value="number") int number){
		return dr.getPopularDiscussions();
	}
	*/
	
	@SuppressWarnings("unused")
	private static class DiscussionBody{
		public String tekst;
		public String naziv;
		public String username;
	}
	
	
}
