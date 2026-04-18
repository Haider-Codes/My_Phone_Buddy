package com.myphonebuddy.modal;

import com.myphonebuddy.constants.AppConstants;
import lombok.Data;

@Data
public class PageInfo {

    int pageNo = 0;
    int pageSize = AppConstants.PAGINATION_SIZE;
}
