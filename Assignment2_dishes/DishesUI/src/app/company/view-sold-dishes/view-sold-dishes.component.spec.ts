import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSoldDishesComponent } from './view-sold-dishes.component';

describe('ViewSoldDishesComponent', () => {
  let component: ViewSoldDishesComponent;
  let fixture: ComponentFixture<ViewSoldDishesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSoldDishesComponent]
    });
    fixture = TestBed.createComponent(ViewSoldDishesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
