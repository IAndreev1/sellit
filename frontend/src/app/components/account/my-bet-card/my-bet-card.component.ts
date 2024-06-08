import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe, NgClass, NgIf} from "@angular/common";
import {BetDto} from "../../../dtos/betDto";
import {Router} from "@angular/router";

@Component({
  selector: 'app-my-bet-card',
  standalone: true,
  imports: [
    DatePipe,
    NgIf,
    NgClass
  ],
  templateUrl: './my-bet-card.component.html',
  styleUrl: './my-bet-card.component.scss'
})
export class MyBetCardComponent {
  @Input() bet: BetDto;

  @Output() accept = new EventEmitter<BetDto>();
  @Output() delete = new EventEmitter<BetDto>();


  constructor(
    private router: Router,
  ) {
  }
  onAccept(): void {
    this.accept.emit(this.bet);
  }

  onDelete(): void {
    this.delete.emit(this.bet);
  }




}
