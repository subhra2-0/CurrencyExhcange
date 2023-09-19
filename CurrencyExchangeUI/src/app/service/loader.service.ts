import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs';

@Injectable({

  providedIn: 'root'

})

export class LoaderService {

hide() {

  throw new Error('Method not implemented.');

}

show() {

  throw new Error('Method not implemented.');

}

 

private isLoadingSubject = new BehaviorSubject<boolean>(false);

public isLoading$ = this.isLoadingSubject.asObservable();

 

constructor() { }

showLoader() {

  this.isLoadingSubject.next(true);

}

 

hideLoader() {

  this.isLoadingSubject.next(false);

}

 

}