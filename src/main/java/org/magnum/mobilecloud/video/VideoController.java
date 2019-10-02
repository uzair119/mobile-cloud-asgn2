/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import org.apache.http.HttpResponse;
import org.eclipse.jetty.http.HttpParser;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import retrofit.http.Path;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class VideoController {
	
	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */


	@Autowired
	private VideoRepo videoRepo;


	
	@RequestMapping(value="/go",method=RequestMethod.GET)
	public @ResponseBody String goodLuck(){
		return "Good Luck!";
	}

	@RequestMapping(value="/video",method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video video)
	{
		videoRepo.save(video);
		return video;
	}

	@RequestMapping(value="/video/{id}",method=RequestMethod.GET)
	public @ResponseBody Video getVideoById(@PathVariable Long id)
	{
		return videoRepo.findOne(id);
	}


	@RequestMapping(value="/video",method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getAllVideos()
	{
		Collection<Video> list = new ArrayList<>();
		Iterable<Video> videoIterable = videoRepo.findAll();
		for(Video v: videoIterable)
		{
			list.add(v);
		}
		return list;
	}

	@RequestMapping(value="video/{id}/like",method=RequestMethod.POST)
	public @ResponseBody void likeVideo(@PathVariable Long id, Principal p, HttpServletResponse httpResponse) throws IOException {
		if(!videoRepo.exists(id))
			httpResponse.sendError(404);

		Video v = videoRepo.findOne(id);
		Set<String> likedBy = v.getLikedBy();
		if(!likedBy.contains(p.getName()))
		{
			likedBy.add(p.getName());
			v.setLikes(v.getLikes()+1);
			videoRepo.save(v);
			httpResponse.setStatus(200);
		}
		else
			httpResponse.setStatus(400);
	}

	@RequestMapping(value="video/{id}/unlike",method=RequestMethod.POST)
	public @ResponseBody void unlikeVideo(@PathVariable Long id, Principal p, HttpServletResponse httpResponse) throws IOException {
		if(!videoRepo.exists(id))
			httpResponse.sendError(404);
		Video v = videoRepo.findOne(id);

		Set<String> likedBy = v.getLikedBy();
		if(likedBy.contains(p.getName()))
		{
			likedBy.remove(p.getName());
			v.setLikes(v.getLikes()-1);
			videoRepo.save(v);
			httpResponse.setStatus(200);
		}
		else
			httpResponse.setStatus(400);
	}



	@RequestMapping(value="/video/{id}/likedby",method=RequestMethod.GET)
	public @ResponseBody Set<String> getVideoLikedBy(@PathVariable Long id, HttpServletResponse httpResponse) throws IOException {
		if(!videoRepo.exists(id))
			httpResponse.sendError(404);
		Video v = videoRepo.findOne(id);
		httpResponse.setStatus(200);
		return v.getLikedBy();

	}

	@RequestMapping(value="/video/search/findByDurationLessThan",method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findVideoByDurationLessThan(@RequestParam Long duration)
	{
		return videoRepo.findByDurationLessThan(duration);
	}

	@RequestMapping(value="/video/search/findByName",method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findVideoByName(@RequestParam String title)
	{
		return videoRepo.findByName(title);
	}
}
