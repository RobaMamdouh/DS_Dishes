import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SoldDishDetailComponent } from './sold-dish-detail.component';

describe('SoldDishDetailComponent', () => {
  let component: SoldDishDetailComponent;
  let fixture: ComponentFixture<SoldDishDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SoldDishDetailComponent]
    });
    fixture = TestBed.createComponent(SoldDishDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
