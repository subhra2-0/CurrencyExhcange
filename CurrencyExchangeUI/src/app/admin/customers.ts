enum GenderEnum {

  MALE,

  FEMALE

}

export enum RoleEnum {

 SUPERUSER='SUPERUSER' ,

CUSTOMER='CUSTOMER'

}

export class Customers {

customerName:string;

email:string;

contactNumber:string;

gender:GenderEnum;

role:RoleEnum;

customerId:number;



// private String customerName;

// private GenderEnum gender;

// private RoleEnum role;

// private Long customerId;

// private String email;

// private Long contactNumber;

// @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")

// private LocalDateTime onboardDate;

// private List<AccountDetailsResponse> accountDetailsResponse;









}