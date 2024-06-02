import { Component, Input, Output, EventEmitter } from '@angular/core';
import {BetDto} from "../../../dtos/betDto";
import {DatePipe, NgIf} from "@angular/common";


@Component({
  selector: 'app-bet-card',
  standalone: true,
  templateUrl: './bet-card.component.html',
  imports: [
    DatePipe,
    NgIf
  ],
  styleUrls: ['./bet-card.component.scss']
})
export class BetCardComponent {
  @Input() bet: BetDto;
  @Output() accept = new EventEmitter<BetDto>();
  @Output() reject = new EventEmitter<BetDto>();

  onAccept(): void {
    this.accept.emit(this.bet);
  }

  onReject(): void {
    this.reject.emit(this.bet);
  }
}
