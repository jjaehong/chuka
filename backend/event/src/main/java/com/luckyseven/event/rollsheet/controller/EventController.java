package com.luckyseven.event.rollsheet.controller;

import com.luckyseven.event.common.exception.BigFileException;
import com.luckyseven.event.common.exception.EmptyFileException;
import com.luckyseven.event.common.exception.NotValidExtensionException;
import com.luckyseven.event.rollsheet.dto.CreateEventDto;
import com.luckyseven.event.rollsheet.dto.EventDto;
import com.luckyseven.event.rollsheet.service.EventService;
import com.luckyseven.event.util.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Tag(name = "Event", description = "이벤트(롤링페이퍼) API")
public class EventController {

    private final EventService eventService;
    private final FileService fileService;


    @PostMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "이벤트 등록", description = "이벤트를 등록(생성)한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "비어있는 파일"),
            @ApiResponse(responseCode = "413", description = "20MB를 초과하는 파일"),
            @ApiResponse(responseCode = "415", description = "지원하지 않는 확장자"),
            @ApiResponse(responseCode = "500", description = "서버 오류"),
            @ApiResponse(responseCode = "503", description = "서버 오류 (File IO)")
    })
    public ResponseEntity<?> createEvent(@ModelAttribute CreateEventDto createEventDto) {   //java.lang.IllegalArgumentException: No enum constant com.luckyseven.event.rollsheet.entity.EventType.생일
        EventDto event = null;

        try {
            event = eventService.createEvent(createEventDto);
        } catch (EmptyFileException e) {
            //400
            return ResponseEntity.status(400).body("파일이 비어있습니다.");
        } catch (BigFileException e) {
            //413
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("업로드한 파일의 용량이 20MB 이상입니다.");
        } catch (NotValidExtensionException e) {
            //415
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("지원하는 확장자가 아닙니다. 지원하는 이미지 형식: jpg, png, jpeg, gif, webp");
        } catch (IOException e) {
            //415
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("지원하는 확장자가 아닙니다. 지원하는 이미지 형식: jpg, png, jpeg, gif, webp");
        }

        return ResponseEntity.status(200).body(event);
    }
}
