import { Component, NgModule } from '@angular/core';
import { Router } from '@angular/router';
import { CustomerServiceService } from 'src/app/service/customer-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})


export class HeaderComponent {
  constructor(public service:CustomerServiceService,private route:Router) {
   
    
  }
  logout(){
    
    localStorage.removeItem("token");
    localStorage.removeItem("loginstatus")
    sessionStorage.clear();
    localStorage.clear();
    this.route.navigateByUrl("/login")
  }

}
