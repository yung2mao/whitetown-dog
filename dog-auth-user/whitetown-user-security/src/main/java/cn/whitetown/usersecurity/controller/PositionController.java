package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.PositionQuery;
import cn.whitetown.authcommon.entity.dto.PositionDto;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.authea.modo.WhiteControlType;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.updown.util.ExcelUtil;
import cn.whitetown.usersecurity.downentity.PositionTemplate;
import cn.whitetown.usersecurity.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 职位管理
 * @author taixian
 * @date 2020/07/20
 **/
@RestController
@RequestMapping("/position")
@WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "position_manager")
public class PositionController {

    @Autowired
    private PositionService positionService;

    private ExcelUtil excelUtil = ExcelUtil.getInstance();

    /**
     * 分页查询职位信息
     * @param positionQuery
     * @return
     */
    @GetMapping("/page")
    public ResponseData<ResponsePage<PositionDto>> queryPositions(PositionQuery positionQuery){
        WhiteToolUtil.defaultPage(positionQuery);
        ResponsePage<PositionDto> positions = positionService.queryPagePositions(positionQuery);
        return ResponseData.ok(positions);
    }

    /**
     * 根据部门ID查询部门的所有职位
     * @param deptId
     * @return
     */
    @GetMapping("/depts")
    public ResponseData<List<PositionDto>> queryPositionByDeptCode(@NotNull(message = "部门ID不能为空") Long deptId){
        List<PositionDto> positionDtoList = positionService.queryDeptPosition(deptId);
        return ResponseData.ok(positionDtoList);
    }

    /**
     * 职位信息下载
     * @param positionQuery
     * @param response
     */
    @GetMapping("/downloads")
    public void download(PositionQuery positionQuery, HttpServletResponse response) {
        positionQuery.setPage(1);
        positionQuery.setSize(DogBaseConstant.DOWN_FILE_MAX_ROW);
        ResponsePage<PositionDto> positions = positionService.queryPagePositions(positionQuery);
        String fileName = "position_" + WhiteFormatUtil.dateFormat("yyyy-MM-dd",new Date());
        String sheetName = "sheet0";
        try {
            excelUtil.writeWebExcel(response, positions.getResultList(), fileName, sheetName, PositionTemplate.class);
        }catch (Exception e) {
            throw new WhResException(ResponseStatusEnum.DOWN_FILE_ERROR);
        }
    }

    /**
     * 添加职位信息
     * @param position
     * @return
     */
    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "position_add_button")
    public ResponseData addPosition(@RequestBody @Valid PositionInfo position) {
        positionService.addPosition(position);
        return ResponseData.ok();
    }

    /**
     * 修改职位信息
     * @param position
     * @return
     */
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "position_update_button")
    public ResponseData updatePosition(@RequestBody @Valid PositionInfo position) {
        positionService.updatePosition(position);
        return ResponseData.ok();
    }

    /**
     * 删除职位信息
     * @param positionId
     * @return
     */
    @GetMapping("/del")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "position_del_button")
    public ResponseData deletePosition(@NotNull(message = "职位ID不能为空") Long positionId) {
        positionService.updatePositionStatus(positionId, DogBaseConstant.DELETE_ERROR);
        return ResponseData.ok();
    }
}
