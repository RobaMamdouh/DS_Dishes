import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllCompanyComponent } from './view-all-company.component';

describe('ViewAllCompanyComponent', () => {
  let component: ViewAllCompanyComponent;
  let fixture: ComponentFixture<ViewAllCompanyComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewAllCompanyComponent]
    });
    fixture = TestBed.createComponent(ViewAllCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
