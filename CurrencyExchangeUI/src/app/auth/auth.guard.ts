import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable ,of} from 'rxjs';
import { CustomerServiceService } from '../service/customer-service.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router:Router,private authService:CustomerServiceService){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean>  {
    if(this.authService.isloggedIn()){
return of(true);
    }else
    {
this.router.navigate(['/login']);
return of(false);
    }
      
  }
  
 
}