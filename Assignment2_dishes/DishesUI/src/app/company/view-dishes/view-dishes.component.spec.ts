import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDishesComponent } from './view-dishes.component';

describe('ViewDishesComponent', () => {
  let component: ViewDishesComponent;
  let fixture: ComponentFixture<ViewDishesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewDishesComponent]
    });
    fixture = TestBed.createComponent(ViewDishesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
