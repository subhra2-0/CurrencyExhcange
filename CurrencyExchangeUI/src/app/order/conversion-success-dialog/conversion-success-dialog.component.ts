import { Component,OnInit,Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-conversion-success-dialog',
  templateUrl: './conversion-success-dialog.component.html',
  styleUrls: ['./conversion-success-dialog.component.css']
})
export class ConversionSuccessDialogComponent implements OnInit {
  txn:any;

  constructor(@Inject(MAT_DIALOG_DATA) public data:any){}
  ngOnInit(): void {
      this.txn= this.data.txn.txnId;
  }
}
