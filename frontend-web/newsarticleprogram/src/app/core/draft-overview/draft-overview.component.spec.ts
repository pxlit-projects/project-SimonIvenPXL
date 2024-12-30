import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DraftOverviewComponent } from './draft-overview.component';

describe('DraftOverviewComponent', () => {
  let component: DraftOverviewComponent;
  let fixture: ComponentFixture<DraftOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DraftOverviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DraftOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
