package rw.cozy.cozybackend.serviceImpl;//package rw.ekimina.authservice.serviceImpl;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.Transformation;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@AllArgsConstructor
//public class CloudinaryService {
//
//    private final Cloudinary cloudinary;
//
//
//
//    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
//        try {
//            // Basic upload
//            Map<String, Object> uploadResult = cloudinary.uploader()
//                    .upload(file.getBytes(),
//                            Map.of("resource_type", "auto"));
//            return uploadResult;
//        } catch (IOException e) {
//            throw new IOException("Error uploading file to Cloudinary", e);
//        }
//    }
//
//    // Optional: Upload with custom options
//    public Map<String, Object> uploadWithTransformation(MultipartFile file) throws IOException {
//        Map<String, Object> uploadOptions = new HashMap<>();
//        uploadOptions.put("folder", "profile_pictures");
//        uploadOptions.put("transformation",
//                new Transformation()
//                        .width(500)
//                        .height(500)
//                        .crop("limit")
//        );
//
//        return cloudinary.uploader().upload(file.getBytes(), uploadOptions);
//    }
//
//    // Method to delete an image
//    public Map<String, Object> deleteImage(String publicId) throws IOException {
//        return cloudinary.uploader().destroy(publicId,
//                Map.of("resource_type", "image"));
//    }
//}
