import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe} from "@angular/common";
import {BetDto} from "../../../dtos/betDto";

@Component({
  selector: 'app-my-bet-card',
  standalone: true,
    imports: [
        DatePipe
    ],
  templateUrl: './my-bet-card.component.html',
  styleUrl: './my-bet-card.component.scss'
})
export class MyBetCardComponent {
  @Input() bet: BetDto;

  @Output() accept = new EventEmitter<BetDto>();
  @Output() delete = new EventEmitter<BetDto>();

  onAccept(): void {
    this.accept.emit(this.bet);
  }

  onDelete(): void {
    this.delete.emit(this.bet);
  }


}
