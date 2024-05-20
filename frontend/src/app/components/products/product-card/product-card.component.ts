import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [],
  templateUrl: './product-card.component.html',
  styleUrl: './product-card.component.scss'
})
export class ProductCardComponent {
@Input() id:string;
@Input() name: string;
@Input() description: string;
@Input() price:number;
@Input() imageDate: string;



  truncateCategoryTitle(title: string): string {
    const maxCharLength: number = 15;
    return title.length > maxCharLength ? title.substring(0, maxCharLength) + '...' : title;
  }
}
