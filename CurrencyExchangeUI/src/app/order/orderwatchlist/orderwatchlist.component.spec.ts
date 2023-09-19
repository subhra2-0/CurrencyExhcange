import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderwatchlistComponent } from './orderwatchlist.component';

describe('OrderwatchlistComponent', () => {
  let component: OrderwatchlistComponent;
  let fixture: ComponentFixture<OrderwatchlistComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderwatchlistComponent]
    });
    fixture = TestBed.createComponent(OrderwatchlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
