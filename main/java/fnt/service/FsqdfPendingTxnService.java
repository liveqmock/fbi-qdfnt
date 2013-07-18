package fnt.service;

import fnt.repository.dao.FsQdfPendingTxnMapper;
import fnt.repository.dao.FsQdfPendingVchInfoMapper;
import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.FsQdfPendingTxnExample;
import fnt.repository.model.FsQdfPendingVchInfo;
import fnt.repository.model.FsQdfPendingVchInfoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * ดýฒนฦฑ
*/
@Service
public class FsqdfPendingTxnService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfPendingTxnService.class);
    @Autowired
    private FsQdfPendingVchInfoMapper fsQdfPendingVchInfoMapper;
    @Autowired
    private FsQdfPendingTxnMapper fsQdfPendingTxnMapper;

    public List<FsQdfPendingVchInfo> qryPendingVchInfosByDate(String date8) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andAddDateEqualTo(date8);
        example.or().andConfirmDateEqualTo(date8);
        example.or().andChkDateEqualTo(date8);
        example.setOrderByClause("QDF_CFM_FLAG");
        return fsQdfPendingVchInfoMapper.selectByExample(example);
    }

    public List<FsQdfPendingVchInfo> qryCfmChkPendingVchInfosByDate(String date8) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andConfirmDateEqualTo(date8);
        example.or().andChkDateEqualTo(date8);
        example.setOrderByClause("QDF_CFM_FLAG");
        return fsQdfPendingVchInfoMapper.selectByExample(example);
    }

    public List<FsQdfPendingTxn> qryPendingTxnsByDate(String date8) {
        FsQdfPendingTxnExample example = new FsQdfPendingTxnExample();
        example.createCriteria().andJyrqEqualTo(date8);
        example.setOrderByClause("BMKYWXH");
        return fsQdfPendingTxnMapper.selectByExample(example);
    }

    public List<FsQdfPendingVchInfo> qryPendingVchInfosByAddDate(String date8) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andAddDateEqualTo(date8);
        example.setOrderByClause("QDF_CFM_FLAG, BMKYWXH");
        return fsQdfPendingVchInfoMapper.selectByExample(example);
    }

    public FsQdfPendingVchInfo qryPendingVchInfoByBmkywxh(String bmkywxh) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andBmkywxhEqualTo(bmkywxh);
        List<FsQdfPendingVchInfo> vchInfos = fsQdfPendingVchInfoMapper.selectByExample(example);
        return vchInfos.size() > 0 ? vchInfos.get(0) : null;
    }
}
