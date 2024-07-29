package com.syamsandi.java_rs_rawat_jalan.service;

import org.springframework.stereotype.Service;

@Service
public class SlugUtilsImpl implements SlugUtils {
  @Override
  public String toSlug(String input) {
    if (input == null) {
      return null;
    }
    return input.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
  }
}
