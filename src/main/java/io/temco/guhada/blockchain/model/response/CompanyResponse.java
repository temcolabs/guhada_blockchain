//package io.temco.guhada.blockchain.model.response;
//
//import io.temco.guhada.blockchain.model.Company;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class CompanyResponse {
//
//    private int companyId;
//    private String companyName;
//    private long monthLimit;
//    private long dayLimit;
//    private String apiToken;
//    private String category;
//
//    public static CompanyResponse of(Company company){
//        CompanyResponse companyResponse = new CompanyResponse();
//        companyResponse.companyId = company.getCompanyId();
//        companyResponse.companyName = company.getCompanyName();
//        companyResponse.monthLimit = company.getMonthLimit();
//        companyResponse.dayLimit = company.getDayLimit();
//        companyResponse.apiToken = company.getApiToken();
//        companyResponse.category = company.getCategory();
//        return companyResponse;
//    }
//}
