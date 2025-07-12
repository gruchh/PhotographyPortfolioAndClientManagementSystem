import { Component, OnDestroy, OnInit } from "@angular/core";
import { PaginatedResponse, Photo } from "../../../../core/models/photo.model";
import { Subject, takeUntil } from "rxjs";
import { environment } from "../../../../../environments/environment";
import { PhotoService } from "../../services/photo.service";

@Component({
  selector: 'app-portfolio-grid',
  templateUrl: './portfolio-grid.component.html',
  styleUrls: ['./portfolio-grid.component.css']
})
export class PortfolioGridComponent implements OnInit, OnDestroy {
  photos: Photo[] = [];
  loading = false;
  error: string | null = null;

  currentPage = 0;
  pageSize = 12;
  totalElements = 0;
  totalPages = 0;

  private destroy$ = new Subject<void>();

  constructor(private photoService: PhotoService) {}

  ngOnInit(): void {
    this.loadPhotos();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadPhotos(page: number = 0): void {
    this.loading = true;
    this.error = null;

    this.photoService.getPhotos(page, this.pageSize)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: PaginatedResponse<Photo>) => {
          this.photos = response.content;
          this.currentPage = response.pageable.pageNumber;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading photos:', error);
          this.error = 'Nie udało się załadować zdjęć. Spróbuj ponownie.';
          this.loading = false;
        }
      });
  }

  /**
   * Obsługuje zmianę strony
   */
  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadPhotos(page);
    }
  }

  getPhotoUrl(photo: Photo): string {
    return `${environment.apiUrl}/${photo.filePath}`;
  }

  getThumbnailUrl(photo: Photo): string {
    return `${environment.apiUrl}/${photo.thumbnailPath}`;
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('pl-PL');
  }

  trackByPhotoId(index: number, photo: Photo): number {
    return photo.id;
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;

    if (this.totalPages <= maxVisiblePages) {
      for (let i = 0; i < this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      const start = Math.max(0, this.currentPage - Math.floor(maxVisiblePages / 2));
      const end = Math.min(this.totalPages, start + maxVisiblePages);

      for (let i = start; i < end; i++) {
        pages.push(i);
      }
    }

    return pages;
  }
}
