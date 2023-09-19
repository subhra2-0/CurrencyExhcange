import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAccountDailogComponent } from './create-account-dailog.component';

describe('CreateAccountDailogComponent', () => {
  let component: CreateAccountDailogComponent;
  let fixture: ComponentFixture<CreateAccountDailogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateAccountDailogComponent]
    });
    fixture = TestBed.createComponent(CreateAccountDailogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
