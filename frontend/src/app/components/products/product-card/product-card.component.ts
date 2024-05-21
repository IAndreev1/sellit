import {Component, Input, OnInit} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [
    NgIf,
    NgOptimizedImage
  ],
  templateUrl: './product-card.component.html',
  styleUrl: './product-card.component.scss'
})
export class ProductCardComponent implements OnInit {
  @Input() id: string;
  @Input() name: string;
  @Input() description: string;
  @Input() price: number;
  @Input() imageData: string;
  decodedImage: string | null = null;


  truncateCategoryTitle(title: string): string {
    const maxCharLength: number = 15;
    return title.length > maxCharLength ? title.substring(0, maxCharLength) + '...' : title;
  }

  decodeImage(): void {
    if (this.imageData != null) {
      if (!this.imageData.startsWith('data:image')) {
        this.imageData = 'data:image/jpeg;base64,' + this.imageData;
      }
    }
    this.decodedImage = this.imageData;
  }

  ngOnInit(): void {

    this.decodeImage();
  }

  getCardBackground(): string {
    if (!this.decodedImage) {
      return this.getRandomColor(); // If there's no image, return a random color
    } else {
      return ''; // If there is an image, return empty string (or any other background style)
    }
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
}
