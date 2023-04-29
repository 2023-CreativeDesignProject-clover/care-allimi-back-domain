package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.service.FacilityService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FacilityController {
  private final FacilityService facilityService;

  //시설 추가
  @PostMapping("/v2/facilities")
  public ResponseEntity addFacility(@RequestBody AddFacilityDTO dto) { // name, address, tel, fm_name

    if (dto.getName() == null) {
      log.info("FacilityController 시설추가: 시설 이름은 필수임");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Long facilityId;

    try {
        facilityId = facilityService.addFacility(dto);
    } catch(Exception exception) { //그냥 에러
      log.info("FacilityController 시설추가: 에러남");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("facility_id", facilityId);

      return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  //시설 삭제
  @DeleteMapping("/v2/facilities")
  public ResponseEntity deleteFacility(@RequestBody Map<String, Long> facility) {
    Long facilityId = facility.get("facility_id");

    if (facilityId == null) {
      log.info("FacilityController 시설 삭제: facility_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      facilityService.deleteFacility(facilityId);
    } catch (Exception e) {
      log.info("FacilityController 시설 삭제: 에러남");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //시설 수정 - 빈 값이 들어왔을 때 어떻게 처리할지 test
  @PatchMapping("/v2/facilities")
  public ResponseEntity modifyFacility(@RequestBody EditFacilityDTO dto) { // facility_id, name, address, tel, fm_name
    Long facilityId = dto.getFacility_id();

    if (facilityId == null) {
      log.info("FacilityController 시설 수정: facility_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      facilityId = facilityService.editFacility(dto);
    } catch(FacilityException exception) { //그냥 에러
      log.info("FacilityController 시설추가: 해당 시설을 찾을 수 없음");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch(Exception exception) { //그냥 에러
      log.info("FacilityController 시설추가: 에러남");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("facility_id", facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }


  //시설 전체보기 - 관리자용
  @GetMapping("/v2/facilities/admin")
  public ResponseEntity adminFacilityList(@PageableDefault(size=20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
    Page<Facility> facilities;

    try {
      facilities = facilityService.findAll(pageable);
    } catch (Exception e) {
      log.info("FacilityController 시설 전체보기 관리자용: 에러남");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(facilities.getContent());
  }
}
