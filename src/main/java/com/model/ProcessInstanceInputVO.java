package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.dingtalk.api.request.OapiProcessinstanceCreateRequest.FormComponentValueVo;
import org.springframework.util.CollectionUtils;

/**
 * @author 令久
 * @date 2018/08/22
 * 审批实例
 */
public class ProcessInstanceInputVO {
    private String approvers;

    private String ccList;

    private String ccPosition;

    private Long deptId;

    private List<TextForm> textForms;

    private List<PictureForm> pictureForms;

    private List<DetailForm> detailForms;

    private String originatorUserId;

    public static class TextForm {
        private String name;

        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class PictureForm {
        private String name;

        private List<String> value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

    public static class DetailForm {
        private String name;

        private List<TextForm> textForms;

        private List<PictureForm> pictureForms;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TextForm> getTextForms() {
            return textForms;
        }

        public void setTextForms(List<TextForm> textForms) {
            this.textForms = textForms;
        }

        public List<PictureForm> getPictureForms() {
            return pictureForms;
        }

        public void setPictureForms(List<PictureForm> pictureForms) {
            this.pictureForms = pictureForms;
        }
    }

    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public String getCcList() {
        return ccList;
    }

    public void setCcList(String ccList) {
        this.ccList = ccList;
    }

    public String getCcPosition() {
        return ccPosition;
    }

    public void setCcPosition(String ccPosition) {
        this.ccPosition = ccPosition;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public List<TextForm> getTextForms() {
        return textForms;
    }

    public void setTextForms(List<TextForm> textForms) {
        this.textForms = textForms;
    }

    public List<PictureForm> getPictureForms() {
        return pictureForms;
    }

    public void setPictureForms(List<PictureForm> pictureForms) {
        this.pictureForms = pictureForms;
    }

    public List<DetailForm> getDetailForms() {
        return detailForms;
    }

    public void setDetailForms(List<DetailForm> detailForms) {
        this.detailForms = detailForms;
    }

    public String getOriginatorUserId() {
        return originatorUserId;
    }

    public void setOriginatorUserId(String originatorUserId) {
        this.originatorUserId = originatorUserId;
    }

    public List<FormComponentValueVo> generateForms() {
        List<FormComponentValueVo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(textForms)) {
            for (TextForm textForm : textForms) {
                result.add(generateFormWithTextForm(textForm));
            }
        }

        if (!CollectionUtils.isEmpty(pictureForms)) {
            for (PictureForm pictureForm : pictureForms) {
                result.add(generateFormWithPictureForm(pictureForm));
            }
        }

        if (!CollectionUtils.isEmpty(detailForms)) {
            for (DetailForm detailForm : detailForms) {
                result.add(generateFormWithDetailForm(detailForm));
            }
        }
        return result;
    }

    private FormComponentValueVo generateFormWithTextForm(TextForm textForm) {
        FormComponentValueVo form = new FormComponentValueVo();
        form.setName(textForm.getName());
        form.setValue(textForm.getValue());
        return form;
    }

    private FormComponentValueVo generateFormWithPictureForm(PictureForm pictureForm) {
        FormComponentValueVo form = new FormComponentValueVo();
        form.setName(pictureForm.getName());
        form.setValue(JSON.toJSONString(pictureForm.getValue()));
        return form;
    }

    private FormComponentValueVo generateFormWithDetailForm(DetailForm detailForm) {
        FormComponentValueVo form = new FormComponentValueVo();
        form.setName(detailForm.getName());

        List<FormComponentValueVo> forms = new ArrayList<>();
        if (!CollectionUtils.isEmpty(detailForm.getTextForms())) {
            for (TextForm textForm : detailForm.getTextForms()) {
                forms.add(generateFormWithTextForm(textForm));
            }
        }

        if (!CollectionUtils.isEmpty(detailForm.pictureForms)) {
            for (PictureForm pictureForm : detailForm.pictureForms) {
                forms.add(generateFormWithPictureForm(pictureForm));
            }
        }

        form.setValue(JSON.toJSONString(Arrays.asList(forms)));

        return form;
    }
}
