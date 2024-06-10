//package com.essay.zaloapp.config;
//
//import com.essay.zaloapp.domain.enums.Sex;
//import com.essay.zaloapp.domain.enums.Status;
//import com.essay.zaloapp.domain.models.CategoryUser;
//import com.essay.zaloapp.domain.models.User;
//import com.essay.zaloapp.domain.models.UserOptionPoll;
//import com.essay.zaloapp.domain.payload.request.SocialMedia.CreateNewPostRequest;
//import com.essay.zaloapp.repository.UserRepository;
//import com.essay.zaloapp.services.SocialMediaService;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@AllArgsConstructor
//public class StartRunner implements ApplicationRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private SocialMediaService socialMediaService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        List<String> titles = Arrays.asList(
//                "Bình minh tuyệt đẹp tại biển Đà Nẵng", "Hoàng hôn rực rỡ trên đỉnh Fansipan", "Cảnh núi non trùng điệp ở Sapa", "Mặt hồ gương sáng tại Đà Lạt",
//                "Thác nước hùng vĩ ở Bản Giốc", "Cảnh đẹp yên bình tại Hội An", "Dòng sông uốn lượn ở Ninh Bình", "Rừng thông bạt ngàn tại Pleiku",
//                "Biển xanh cát trắng tại Phú Quốc", "Đồi chè mênh mông ở Mộc Châu", "Vẻ đẹp thiên nhiên ở Cần Thơ", "Cánh đồng hoa hướng dương tại Nghệ An",
//                "Làng quê yên bình ở Bắc Bộ", "Cảnh sắc mùa thu ở Hà Nội", "Sông nước miền Tây Nam Bộ", "Cầu Rồng lung linh về đêm", "Cảnh đẹp thiên nhiên tại Vũng Tàu",
//                "Vườn quốc gia Bạch Mã", "Biển Cửa Đại xanh ngắt", "Cảnh đẹp trên đỉnh núi Bà Nà", "Phong cảnh mùa xuân tại Tam Đảo", "Sông Hương thơ mộng ở Huế",
//                "Kỳ quan thiên nhiên Vịnh Hạ Long", "Làng cổ Đường Lâm yên bình", "Biển trời lộng lẫy tại Nha Trang", "Đỉnh núi Lang Biang hùng vĩ", "Cánh đồng lúa bát ngát ở Tây Bắc",
//                "Đồi cát trắng tại Mũi Né", "Hồ Tây lung linh dưới ánh hoàng hôn", "Phong cảnh miền núi Tây Nguyên", "Làng hoa Sa Đéc rực rỡ", "Cánh đồng hoa tam giác mạch Hà Giang",
//                "Vườn hoa thành phố Đà Lạt", "Biển Mỹ Khê trong xanh", "Rừng tràm Trà Sư bạt ngàn", "Phố cổ Hội An lung linh về đêm", "Vẻ đẹp biển Cô Tô",
//                "Dòng sông Hậu yên bình", "Cảnh đẹp thiên nhiên ở Đồng Tháp", "Đồi cát bay Phan Thiết", "Đèo Hải Vân hùng vĩ", "Biển Hồ Tràm thơ mộng",
//                "Phong cảnh xanh tươi tại Bình Định", "Rừng quốc gia Nam Cát Tiên", "Hồ Núi Cốc yên bình", "Cảnh đẹp núi rừng Ba Vì", "Thác Bản Giốc đẹp như tranh",
//                "Cánh đồng cỏ lau Hà Nội", "Núi non hùng vĩ ở Quảng Bình", "Phong cảnh thanh bình ở Đồng Nai", "Vẻ đẹp yên bình miền Tây Nam Bộ", "Biển đảo Lý Sơn",
//                "Rừng nguyên sinh Cúc Phương", "Đảo ngọc Cát Bà", "Làng chài yên bình Phú Quốc", "Đỉnh núi Ngũ Hành Sơn", "Rừng tràm Tân Lập mênh mông",
//                "Hồ Ba Bể thanh bình", "Bãi biển Quy Nhơn xanh biếc", "Đỉnh Phan Xi Păng hùng vĩ", "Phong cảnh làng quê Bắc Bộ", "Cánh đồng muối Ninh Thuận",
//                "Cảnh đẹp núi rừng Hòa Bình", "Hồ Gươm lung linh về đêm", "Thác Datanla Đà Lạt", "Phong cảnh thiên nhiên tại Buôn Mê Thuột", "Rừng quốc gia Phong Nha - Kẻ Bàng",
//                "Cảnh đẹp biển Vĩnh Hy", "Cánh đồng cỏ lau Hà Giang", "Núi rừng hoang sơ Tây Bắc", "Vườn quốc gia Yok Đôn", "Biển Đại Lãnh xanh biếc",
//                "Phong cảnh miền núi Bắc Giang", "Làng chài Thanh Nam yên bình", "Đỉnh núi Hoàng Liên Sơn", "Biển Ninh Chữ thơ mộng", "Thung lũng Mai Châu yên bình",
//                "Phong cảnh thiên nhiên tại Lâm Đồng", "Rừng tràm Long An", "Đồi cát Quang Phú", "Hồ Đồng Nai thanh bình", "Cảnh đẹp thiên nhiên ở Hà Nam",
//                "Núi rừng xanh ngắt Lạng Sơn", "Biển Cửa Lò rực rỡ", "Vườn quốc gia U Minh Thượng", "Phong cảnh làng quê Hà Giang", "Cánh đồng lúa bát ngát Nghệ An",
//                "Đỉnh núi Bạch Mã", "Biển Bà Rịa - Vũng Tàu xanh ngắt", "Phong cảnh miền Tây sông nước", "Làng hoa Nhật Tân rực rỡ", "Hồ Trị An thanh bình",
//                "Đồi chè Thanh Chương", "Núi rừng Tây Bắc hoang sơ", "Biển Cần Giờ xanh biếc", "Rừng tràm Gáo Giồng", "Phong cảnh miền núi Yên Bái",
//                "Hồ Tây trong lành", "Đỉnh núi Tà Xùa", "Biển Nha Trang rực rỡ", "Vườn quốc gia Lò Gò - Xa Mát", "Núi rừng Lâm Đồng hùng vĩ",
//                "Biển Mỹ Khê xanh ngát", "Cánh đồng lúa chín vàng", "Phong cảnh thiên nhiên Trà Vinh", "Đồi chè Phú Thọ", "Núi rừng Điện Biên",
//                "Biển đảo Côn Đảo", "Rừng quốc gia Nam Động", "Phong cảnh yên bình Quảng Trị", "Hồ Tuyền Lâm thơ mộng", "Biển Cam Ranh xanh biếc");
//        Random random = new Random();
//        List<User> users = (List<User>) userRepository.findAll();
//        List<File> resources = getPostResources("D:\\Pictures\\KLTN\\PostResources");
//        for (User user : users) {
//            for (int i = 0; i < 10; i++) {
//                String title = titles.get(random.nextInt(titles.size()));
//                MultipartFile[] selectedFiles = new MultipartFile[2];
//
//                for (int j = 0; j < 2; j++) {
//                    File selectedFile = resources.get(random.nextInt(resources.size()));
//                    selectedFiles[j] = convertFileToMultipartFile(selectedFile);
//                }
//
//                CreateNewPostRequest request = new CreateNewPostRequest();
//                request.setContent(title);
//                request.setAudience("Public");
//                request.setFiles(selectedFiles);
//
//                String result = socialMediaService.createNewPost(user.getId(), request);
//                System.out.println(result);
//            }
//        }
//    }
//
//    private List<File> getPostResources(String folderPath) {
//        File folder = new File(folderPath);
//        return Arrays.stream(folder.listFiles())
//                .filter(file -> !file.isDirectory() && (file.getName().endsWith(".jfif") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg") || file.getName().endsWith(".png")))
//                .collect(Collectors.toList());
//    }
//
//    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
//        FileInputStream input = new FileInputStream(file);
//        return new MockMultipartFile(file.getName(), file.getName(), "image/jfif", input);
//    }
//}
