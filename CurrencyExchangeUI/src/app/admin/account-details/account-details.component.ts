import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'src/app/service/account.service';
import { LoaderService } from 'src/app/service/loader.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {

  account: any =null;

  constructor(
    private route: ActivatedRoute,
    private accountService: AccountService
  ) { }

  ngOnInit(): void {
    const accountNumberParam = this.route.snapshot.paramMap.get('accountNumber');
    if (accountNumberParam !== null) {
      const accountNumber = +accountNumberParam;
      this.account = this.accountService.getAccountsList();
    }
  }
}

