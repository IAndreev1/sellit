export class ProductDto {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public price: number,
    public imageData: string,
  ) {
  }
}

export class ProductSearchDto {
  constructor(
    public name: string,
    public description: string,
    public priceFrom: number,
    public priceTo: number,
  ) {
  }
}
