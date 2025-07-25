package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Entity.ReplyEntity;

import java.util.List;

public interface ReplyService {
    //댓글 등록
    ReplyDTO register(ReplyDTO replyDTO);
    //댓글 목록
    List<ReplyDTO> findAll();
    List<ReplyDTO> list(Long id);

    //상세보기
    ReplyDTO read(Long id);
    ReplyDTO findByInquireId(Long id);

    //댓글 수정
    ReplyDTO update(ReplyDTO replyDTO);

}
