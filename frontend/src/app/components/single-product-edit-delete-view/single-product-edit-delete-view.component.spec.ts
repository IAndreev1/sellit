import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleProductEditDeleteViewComponent } from './single-product-edit-delete-view.component';

describe('SingleProductEditDeleteViewComponent', () => {
  let component: SingleProductEditDeleteViewComponent;
  let fixture: ComponentFixture<SingleProductEditDeleteViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SingleProductEditDeleteViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SingleProductEditDeleteViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
