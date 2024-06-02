import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductDto } from '../../dtos/productDto';
import { NavbarComponent } from '../navbar/navbar.component';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { BetService } from '../../services/bet.service';
import { BetDto } from '../../dtos/betDto';
import {BetCardComponent} from "./bet-card/bet-card.component";


@Component({
  selector: 'app-single-product-edit-delete-view',
  standalone: true,
  imports: [
    NavbarComponent,
    NgIf,
    RouterLink,
    DatePipe,
    NgForOf,
    BetCardComponent
  ],
  templateUrl: './single-product-edit-delete-view.component.html',
  styleUrls: ['./single-product-edit-delete-view.component.scss']
})
export class SingleProductEditDeleteViewComponent implements OnInit {

  product: ProductDto;
  decodedImage: string;
  bets: BetDto[];

  constructor(
    private service: ProductService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private betService: BetService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe({
      next: params => {
        const prodId = params.id;
        this.service.getById(prodId).subscribe({
          next: res => {
            this.product = res;
            this.betService.getAllBetsForProduct(this.product).subscribe({
              next: allBets => {
                this.bets = allBets;
              }
            });
            this.decodeImage();
          },
          error: () => {
            this.router.navigate(['/account']);
          }
        });
      },
      error: () => {
        this.router.navigate(['/account']);
      }
    });
  }

  decodeImage(): void {
    if (this.product.imageData != null) {
      if (!this.product.imageData.startsWith('data:image')) {
        this.decodedImage = 'data:image/jpeg;base64,' + this.product.imageData;
      }
    }
  }

  acceptBet(bet: BetDto): void {
    bet.accepted = true;
    this.betService.updateBet(bet).subscribe({
      next: allBets => {
        this.ngOnInit();
      }
    });
    this.notification.success('Bet accepted successfully.');

  }

  rejectBet(bet: BetDto): void {
    // Add your logic to reject the bet here
    this.notification.error('Bet rejected successfully.');
    // Optionally, remove the bet from the list or update its status
    this.bets = this.bets.filter(b => b !== bet);
  }


}
