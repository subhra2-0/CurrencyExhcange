import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAccountDailogComponent } from './update-account-dailog.component';

describe('UpdateAccountDailogComponent', () => {
  let component: UpdateAccountDailogComponent;
  let fixture: ComponentFixture<UpdateAccountDailogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateAccountDailogComponent]
    });
    fixture = TestBed.createComponent(UpdateAccountDailogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
