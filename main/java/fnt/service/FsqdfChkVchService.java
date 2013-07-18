package fnt.service;

import fnt.repository.dao.FsQdfChkTxnMapper;
import fnt.repository.dao.FsQdfChkVchMapper;
import fnt.repository.model.FsQdfChkTxn;
import fnt.repository.model.FsQdfChkTxnExample;
import fnt.repository.model.FsQdfChkVch;
import fnt.repository.model.FsQdfChkVchExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * 市财政非税对账结果
*/
@Service
public class FsqdfChkVchService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfChkVchService.class);
    @Autowired
    private FsQdfChkVchMapper fsQdfChkVchMapper;
    @Autowired
    private FsQdfChkTxnMapper fsQdfChkTxnMapper;

    public List<FsQdfChkVch> qryChkVchs(String date8) {
        FsQdfChkVchExample example = new FsQdfChkVchExample();
        example.createCriteria().andChkDateEqualTo(date8);
        example.setOrderByClause("PJZL,JKSBH,CHK_STS,DATA_SYS_ID");
        return fsQdfChkVchMapper.selectByExample(example);
    }

    public List<FsQdfChkTxn> qryPosChkTxns(String date8) {
        FsQdfChkTxnExample example = new FsQdfChkTxnExample();
        example.createCriteria().andTxnDateEqualTo(date8).andSendSysIdEqualTo("2");
        return fsQdfChkTxnMapper.selectByExample(example);
    }
}
