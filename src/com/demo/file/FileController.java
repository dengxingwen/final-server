package com.demo.file;

import java.util.Map;

import com.demo.base.BaseController;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
 
public class FileController extends BaseController {
	
	public void index() {
		renderJson("{\"state\":0}");
	}
	
	
    public void upload() {
    	UploadFile file = getFile("imgFile", PathKit.getWebRootPath() + "/temp");
        Map<String, Object> map = saveFile(file);
        renderJson(map);
    }
 
    public void download(){
        String path = getPara(0);
        String img = PathKit.getWebRootPath() + "/img/u/" + path.replaceAll("_", "/");
//        ZipUtil.zip(img, PathKit.getWebRootPath() + "/img/temp/" + path);
        renderFile("/img/temp/" + path + ".zip");
    }
 
}