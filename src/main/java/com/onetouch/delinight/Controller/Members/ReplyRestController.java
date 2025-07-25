package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.wiring.BeanWiringInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyRestController {

    private final ReplyService replyService;

    @GetMapping("/list/{inquireId}")
    public ResponseEntity<List<ReplyDTO>> list(@PathVariable("inquireId") Long inquireId){
        log.info("페이지 진입");
        List<ReplyDTO> replyDTOList = replyService.list(inquireId);
        return new ResponseEntity<List<ReplyDTO>>(replyDTOList , HttpStatus.OK);
    }
//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody ReplyDTO replyDTO){
//        log.info("전달받은값 : " + replyDTO);
//        log.info("전달받은값 : " + replyDTO);
//
//        log.info(replyDTO.getId());
//
//        replyService.register(replyDTO);
//
//        return new ResponseEntity<String>("저장되었습니다.",HttpStatus.OK);
//
//    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid ReplyDTO replyDTO, BindingResult bindingResult){
        //@Valid는 ReplyDTO안의 값이 유효한지 검사하고, bindingResult는 그 검사 결과(오류 여부)를 담는다.
        log.info("들어오는 값 : " +replyDTO);

        if(bindingResult.hasErrors()){ //문제가 생겼다면 아래 코드를 실행함
            log.info(bindingResult.getAllErrors()); //어떤 문제가 있었는지 전체 오류 내용을 로그찍음

            List<ObjectError>list =
                    bindingResult.getAllErrors(); //오류들을 하나씩 꺼내서 list에 담는다
            list.forEach(objectError -> log.info(objectError)); //오류 하나하나를 로그에 출력(무슨 문제인지 확인가능)

            return new ResponseEntity<List<ObjectError>>(list,HttpStatus.BAD_REQUEST);
            //잘못된 요청 상태와 함께 사용자에게 보냄 (입력값에 문제가 있다고 알려줌)
        }

        ReplyDTO dto =
                replyService.register(replyDTO);//replyService.register()를 호출해서 DB에
        //저장하고 그 결과를 dto에 담는다
        if(dto == null){ // 저장이 실패했는지 확인 (null이면 저장이 안됨)
            return new ResponseEntity<String>("저장실패", HttpStatus.INTERNAL_SERVER_ERROR);
            //저장이 실패하면 500에로와 함께 "저장실패" 메세지를 사용자에게 보냄
        }else {
            log.info(dto);//저장이 성공했다면 결과를 로그로 출력
            return new ResponseEntity<String>(dto.getId()+"번글이 저장되었습니다.",HttpStatus.OK);
            //저장된 댓글의 번호(id)를 포함한 메세지를 사용자에게 보냄
            //성공시 "몇번글이 저장되었습니다"
        }
    }

}
