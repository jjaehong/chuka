package com.luckyseven.funding.service;

import com.luckyseven.funding.dto.FundingJoinReq;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

public interface SponsorService {
    int joinFunding(int fundingId, FundingJoinReq dto, String userId) throws IllegalStateException;
    String test(FundingJoinReq dto) throws IamportResponseException, IOException;
}
