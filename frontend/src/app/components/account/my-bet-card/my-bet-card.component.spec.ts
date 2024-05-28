import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBetCardComponent } from './my-bet-card.component';

describe('MyBetCardComponent', () => {
  let component: MyBetCardComponent;
  let fixture: ComponentFixture<MyBetCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyBetCardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyBetCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
