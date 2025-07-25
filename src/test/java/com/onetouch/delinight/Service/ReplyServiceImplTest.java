package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Entity.InquireEntity;
import com.onetouch.delinight.Entity.ReplyEntity;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

@SpringBootTest
@Log4j2
@Transactional
class ReplyServiceImplTest {
//    @Autowired
//    ReplyRepository replyRepository;
//    @Autowired
//    InquireRepository inquireRepository;
//    @Autowired
//    ReplyService replyService;
//    @Autowired
//    ModelMapper modelMapper;
//
//    @Test
//    @Commit
//    public void register(){
//
//
//        InquireEntity inquireEntity =
//                inquireRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//        log.info("값 :" + inquireEntity);
//        ReplyDTO replyDTO = new ReplyDTO();
//        replyDTO.setId(1L);
//        replyDTO.setReplyText("확인");
//        replyDTO.setReplyer("신라호텔");
//        log.info(replyDTO);
//
//        InquireDTO inquireDTO =
//                modelMapper.map(inquireEntity, InquireDTO.class);
//        replyDTO.setInquireDTO(inquireDTO);
//        log.info(inquireDTO);
//
//        replyService.register(replyDTO);
//
//
//    }
//
//
//    @Test
//    public void list(){
//        List<ReplyDTO> replyDTOList =
//                replyService.list(1L);
//        log.info(replyDTOList);
//    }
//    @Test
//    public void read(){
//        ReplyDTO replyDTO = replyService.read(1L);
//        log.info(replyDTO);
//    }
//    @Test
//    public void update(){
//        ReplyEntity replyEntity = replyRepository.findById(1L).get();
//        replyEntity.setReplyText("승인");
//        replyEntity.setReplyer("신라");
//        ReplyEntity result = replyRepository.save(replyEntity);
//        log.info(result);
//
//    }



}