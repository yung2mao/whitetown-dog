package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.PositionQuery;
import cn.whitetown.authcommon.entity.dto.PositionDto;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.usersecurity.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 职位管理
 * @author taixian
 * @date 2020/07/20
 **/
@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

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
     * 添加职位信息
     * @param position
     * @return
     */
    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    public ResponseData addPosition(@RequestBody @Valid PositionDto position) {
        return null;
    }

    /**
     * 修改职位信息
     * @param position
     * @return
     */
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ResponseData updatePosition(@RequestBody @Valid PositionDto position) {
        return null;
    }

    /**
     * 删除职位信息
     * @param positionId
     * @return
     */
    @GetMapping("/del")
    public ResponseData deletePosition(@NotNull Long positionId) {
        return null;
    }
}
