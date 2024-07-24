package com.syamsandi.java_rs_rawat_jalan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {

  private Integer currentPage;

  private Integer totalPage;

  private Integer size;
}
