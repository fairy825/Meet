package com.meet.controller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.meet.pojo.Book;
import com.meet.pojo.Bookimage;
import com.meet.result.Result;
import com.meet.service.BookService;
import com.meet.service.BookimageService;
import com.meet.utils.ImageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@Api(value="场馆图片的接口", tags= {"场馆图片管理的controller"})
public class BookimageController extends BasicController{

	@Autowired
	BookService bookService;
	@Autowired
	BookimageService bookimageService;

	@GetMapping("/book/{bid}/bookimage")
    public Result<List<Bookimage>> list(@PathVariable("bid") int bid) {
//        Venue venue = venueService.get(vid);
        List<Bookimage> bookimages =  bookimageService.listByBook(bid);
        return Result.success(bookimages);

    }
//	@ApiOperation(value="上传场馆图片新闻", notes="管理员上传场馆图片的接口")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name="vid", value="场馆id", dataType="Integer"),
//	})
    @PostMapping("/bookimage")
    public Result add(@RequestParam("bid") int bid,  MultipartFile image, HttpServletRequest request) throws Exception {
		Bookimage bean = new Bookimage();
//		Book book = venueService.get(bid);
    	bean.setBookId(bid);
		bookimageService.add(bean);

		String folder = FILE_SPACE + "/bookimage/" + bid ;
		String path = folder + "/" + bean.getId()+".jpg";
		System.out.println(path);
		File file = new File(path);
		String fileName = file.getName();
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try {
			image.transferTo(file);
			BufferedImage img = ImageUtil.change2jpg(file);
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}

        	String imageFolder_small= FILE_SPACE+"/bookimage_small/"+ bid;
//        	String imageFolder_middle= FILE_SPACE+"/venueImage_middle/"+ vid;
        	File f_small = new File(imageFolder_small, fileName);
//        	File f_middle = new File(imageFolder_middle, fileName);
        	f_small.getParentFile().mkdirs();
//        	f_middle.getParentFile().mkdirs();
        	ImageUtil.resizeImage(file, 56, 56, f_small);
//        	ImageUtil.resizeImage(file, 217, 190, f_middle);

        return Result.success(null);
    }
    
    @DeleteMapping("/bookimage/{id}")
    public Result delete(@PathVariable("id") int id, HttpServletRequest request) {
		Bookimage bean = bookimageService.get(id);
		bookimageService.delete(id);

		String folder = FILE_SPACE + "/bookimage/" + bean.getBookId() ;

//		File  imageFolder= new File(request.getServletContext().getRealPath(folder));

		String path = folder + "/" + bean.getId()+".jpg";
		File file = new File(path);
		String fileName = file.getName();
		file.delete();
		String imageFolder_small= FILE_SPACE+"/bookimage_small/"+ bean.getBookId();
//		String imageFolder_middle= FILE_SPACE+"/venueImage_middle/"+ bean.getVenue().getId();
		File f_small = new File(imageFolder_small, fileName);
//		File f_middle = new File(imageFolder_middle, fileName);
		f_small.delete();
//		f_middle.delete();

		return Result.success(null);
    }
}