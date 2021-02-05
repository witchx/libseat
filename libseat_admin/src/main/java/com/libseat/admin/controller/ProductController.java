package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.ProductService;
import com.libseat.api.constant.VipCardType;
import com.libseat.api.entity.CouponEntity;
import com.libseat.api.entity.ProductEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.CodeGenerateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author witch
 * 2020/11/10 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/product")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CodeGenerateUtils codeGenerateUtils;

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<ProductEntity>> getProductList (@RequestParam(required = false) Integer id,
                                                                   @RequestParam(required = false) String no,
                                                                   @RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) String des,
                                                                   @RequestParam(required = false) String companyName,
                                                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<ProductEntity> productList = productService.getProductList(id, no, name, des, companyName, page, pageSize);
        if (productList == null || productList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(productList);
        }
    }

    @RequestMapping(value = "/createBatch", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createProduct(@RequestBody List<ProductEntity> productEntities) {
        for (ProductEntity productEntity : productEntities) {
            productEntity.setNo(codeGenerateUtils.generateProductCode(""));
        }
        Integer row = productService.insertProductBatch(productEntities);
        if (row != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createProduct(@RequestBody ProductEntity productEntity) {
        productEntity.setNo(codeGenerateUtils.generateProductCode(""));
        Integer row = productService.insertProduct(productEntity);
        if (row != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateProduct(@PathVariable Integer id, @RequestBody ProductEntity productEntity) {
        if (productEntity != null) {
            productEntity.setId(id);
            if (productService.updateProduct(productEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteProductBatch(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.failed();
        }
        productService.deleteProductBatch(ids);
        return CommonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteProduct (@PathVariable Integer id){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(id);
        productService.deleteProduct(productEntity);
        return CommonResult.failed();
    }
}
