package fnt.service;

import fnt.repository.dao.FsQdfChkVchMapper;
import fnt.repository.dao.FsQdfShareInfoMapper;
import fnt.repository.dao.common.CommonMapper;
import fnt.repository.model.FsQdfChkVch;
import fnt.repository.model.FsQdfChkVchExample;
import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.FsQdfShareInfoExample;
import fnt.repository.model.common.DayGatherData;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * 市财政非税对账结果
*/
@Service
public class CommonService {
    private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
    @Autowired
    private DepService depService;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private FsQdfShareInfoMapper shareInfoMapper;

    public List<DayGatherData> qryDayGatherDataByDate(String date8) {

        String date10 = date8.substring(0, 4) + "-" + date8.substring(4, 6) + "-" + date8.substring(6, 8);
        FsQdfShareInfoExample example = new FsQdfShareInfoExample();
        example.createCriteria().andApplyDateEqualTo(date10);
        // 如果没有获取分成信息
       /* if (shareInfoMapper.countByExample(example) <= 0) {
            boolean isGetShareInfos = depService.txn1533028(date10);
            if (!isGetShareInfos) throw new RuntimeException("获取分成信息失败.");
        }*/
        return commonMapper.qryDayGatherDataByDate(date8);
    }

    // chkdt对账，但是以前记账的不明款
    public List<FsQdfPendingTxn> qryChkedFormerPendingTxns(String chkdt) {
        return commonMapper.qryChkedFormerPendingTxns(chkdt);
    }

}
