package data.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import data.dto.SawonDto;
import data.service.SawonService;
import lombok.RequiredArgsConstructor;
import naver.storage.NcpObjectStorageService;

@Controller
@RequiredArgsConstructor
public class SawonController {
	final SawonService sawonService;
	final NcpObjectStorageService storageService; 
	
	private String imagePath="https://kr.object.ncloudstorage.com/bitcamp-bucket-107/sawon/";
	private String bucketName="bitcamp-bucket-107";
	
	@GetMapping({"/"})
	public String mainpage()
	{
		return "sawon/mainpage";		
	}
	
	@GetMapping({"/list"})
	public String sawonlist(Model model)
	{
		List<SawonDto> list=sawonService.getSelectAllSawon();
		model.addAttribute("list",list);
		model.addAttribute("totalCount",list.size());
		model.addAttribute("imagePath",imagePath);
		
		return "sawon/sawonlist";		
	}
	
	@GetMapping("/form")
	public String sawonform()
	{
		return "sawon/sawonform";		
	}
	
	@PostMapping("/insert")
	public String sawonInsert(@ModelAttribute SawonDto dto,
			@RequestParam("upload") MultipartFile upload)
	{
		if(upload.getOriginalFilename().equals(""))
			dto.setPhoto(null);
		else {
			String photo=storageService.uploadFile(bucketName, "sawon", upload);
			dto.setPhoto(photo);
		}
		
		sawonService.insertSawon(dto);
		return "redirect:./list";
	}
	
	@GetMapping("/detail")
	public String detail(@RequestParam(value="num") int num,Model model)
	{
		SawonDto dto=sawonService.getSawon(num);
		String photo=dto.getPhoto();
		
		model.addAttribute("dto",dto);
		model.addAttribute("imagePath",imagePath);
		
		return "sawon/sawondetail";
	}
	
	@GetMapping("/delete")
	@ResponseBody
	public String deleteSawon(@RequestParam(value="num") int num) 
	{
		//파일명 얻기
		String filename=sawonService.getSawon(num).getPhoto();
		
		//스토리지에서 사진 삭제
		storageService.deleteFile(bucketName, "sawon", filename);
		
		//삭제
		sawonService.deleteSawon(num);
		
		return "redirect:./list";
	}
	
	@GetMapping("/updateform")
	public String updateForm(@RequestParam("num") int num, Model model)
	{
		SawonDto dto=sawonService.getSawon(num);
		model.addAttribute("dto", dto);
		model.addAttribute("imagePath", imagePath);
		return "sawon/updateform";
	}
	
	@PostMapping("/update")
	public String update(
			@ModelAttribute SawonDto dto,
			@RequestParam("upload") MultipartFile upload
			)
	{
		
		if(upload.getOriginalFilename().equals(""))
			dto.setPhoto(null);
		else {
			String photo=storageService.uploadFile(bucketName, "sawon", upload);
			dto.setPhoto(photo);
		}
		
		
	    sawonService.updateSawon(dto);
		
	    return "redirect:./detail?num="+dto.getNum();
	}
	
}
