package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.dto.NoticeEditDto;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.NoticeResponse;
import kr.ac.kumoh.allimi.dto.NoticeWriteDto;
import kr.ac.kumoh.allimi.service.NoticeService;
import kr.ac.kumoh.allimi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

//    @GetMapping("/v1/notices")
//    public ResponseEntity noticeList() {
//        List<NoticeResponse> noticeListRespons = noticeService.noticeList();
//
//        for (NoticeResponse nr : noticeListRespons) {
//            System.out.println(nr.getContent());
//        }
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeListRespons.toArray());
//    }

    @GetMapping("/v1/notices/{user_id}")
    public ResponseEntity noticeList(@PathVariable Long user_id) {
        UserRole userRole = userService.getUserRole(user_id);

        List<NoticeResponse> noticeListResponse;

        if (userRole == UserRole.PROTECTOR) { // 보호자인 경우
            noticeListResponse = noticeService.userNoticeList(user_id);
        } else { // 직원, 시설장인 경우
            noticeListResponse = noticeService.noticeList();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeListResponse.toArray());
    }

    @PostMapping("/v1/notices")
    public ResponseEntity noticeWrite(@RequestBody NoticeWriteDto dto) {
        Notice writeNotice = noticeService.write(dto);
        if (writeNotice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PatchMapping("/v1/notices")
    public ResponseEntity noticeEdit(@RequestBody NoticeEditDto dto) {
        noticeService.edit(dto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/v1/notices")
    public ResponseEntity noticeDelete(@RequestBody Map<String, Long> notice) {

        Long deletedCnt = noticeService.delete(notice.get("notice_id"));

        if (deletedCnt == 0)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/v1/notices/detail/{notice_id}")
    public ResponseEntity notice(@PathVariable("notice_id") Long noticeId) {

        NoticeResponse noticeResponse = noticeService.findNotice(noticeId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeResponse);
    }
}

