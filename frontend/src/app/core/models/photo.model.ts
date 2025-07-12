export interface Photo {
  id: number;
  title: string;
  description?: string;
  fileName: string;
  thumbnailName: string;
  filePath: string;
  thumbnailPath: string;
  featured: boolean;
  publiclyVisible: boolean;
  createdAt: string;
  updatedAt: string;
  photoShoot?: {
    id: number;
    title: string;
    description?: string;
  };
}
export interface PhotoMetadata {
  camera?: string;
  lens?: string;
  settings?: string;
  location?: string;
  dateTaken?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  numberOfElements: number;
}
