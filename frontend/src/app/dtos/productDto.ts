export class ProductDto {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public price: number,
    public imageData: Uint8Array,  // Use Uint8Array for binary data
  ) {}
}
