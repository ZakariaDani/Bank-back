package ma.ensa.bank.image;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    /*
    * path where we will store the image
    * */
    public final String directoryPath = "/home";

    public String uploadImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path directory = Paths.get(directoryPath);
        if(!Files.exists(directory)){
            try {
                Files.createDirectories(directory);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Path imagePath = Paths.get(directory + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imagePath.toString();
    }

    public  byte[] getImage(String imageName) throws IOException {
        Path destination = Paths.get(directoryPath+"\\"+imageName);
        return IOUtils.toByteArray(destination.toUri());
    }

}